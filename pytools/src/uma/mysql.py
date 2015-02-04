'''
Created on Nov 9, 2014
'''
from collections import defaultdict
import  MySQLdb
import yaml


class MySQLTools(object):
    
    def __init__(self, db, user, password, host='localhost', port=3306, ):
        db = db
        host = host
        port = port
        user = user
        password = password
        
        db = MySQLdb.connect(host=host, port=port, user=user, passwd=password, db=db)
        self.cursor = db.cursor() 
            
    def execute(self, what):
        result = self.cursor.execute(what)
        
        return result

    def createTables(self, path, how='print'):
        tmpl = 'CREATE TABLE IF NOT EXISTS %(table_name)s (\n%(columns)s)'
        count = 0
        with open(path) as stream:
            tables = yaml.load(stream)
            # each table has columns an constraint definition
            installed = set()
            while (count < len(tables)):
                for t,content in tables.iteritems():
                    if t in installed:
                        continue
                    depends = content.get('depends')
                    if depends is not None:
                        # check dependencies
                        if len(installed.intersection(depends)) < len(depends):
                            continue
                        
                    coldefs = content.get('columns')
                    if coldefs is None:
                        raise ValueError("No 'columns' in table")
                    columns = map(lambda x:"\t" + ' '.join(x), coldefs.iteritems())
                    columns = ',\n'.join(columns)
                    # add constraints
                    constraints = content.get('constraints')
                    if constraints is not None:
                        constraints = map(lambda x: "\t" + x, constraints)
                        constraints = ',\n'.join(constraints)
                        columns += ',\n' + constraints
                    query = tmpl % {'table_name':t, 'columns':columns,}
                    if how == 'print':
                        print query + '\n'
                    elif how =='on-server':
                        print 'Creating table %s...' % t
                        self.execute(query)
                    else:
                        raise ValueError('Unknown parameter "how"=%s' % how)
                    installed.add(t)
                    count += 1 
        print 'total table count: %i' % count
        
    def dropTables(self, path, how='print'):
        
#        what = "DROP TABLE IF EXISTS %s" % ','.join(tables)
#        self.execute(what)
        what = "DROP TABLE IF EXISTS %s"
        count = 0
        with open(path) as stream:
            tables = yaml.load(stream)
            dropped = set()
            while (count < len(tables)):
                to_skip = set()
                left = set()
                for t,content in tables.iteritems():
                    if t in dropped:
                        continue
                    depends = content.get('depends')
                    if depends is not None:
                        # check dependencies, they cannot be removed at this point
                        to_skip.update(set(depends).difference(dropped))
                    left.add(t)
                to_drop = left.difference(to_skip) 
                # otherwise the table can be dropped
                query = what % ",".join(to_drop)
                if how == 'print':
                    print query + '\n'
                elif how =='on-server':
                    print 'Dropping tables: %s' % ','.join(to_drop)
                    self.execute(query)
                else:
                    raise ValueError('Unknown parameter "how"=%s' % how)
                dropped.update(to_drop)
                count += len(to_drop)
        print 'total table count: %i' % count

        
class MySQLToolsTest(MySQLTools):

    def __init__(self):
        db = 'rserverdb'
        user = 'root'
        password = 'abc123'
        super(MySQLToolsTest, self).__init__(db=db, user=user, password=password)
        
    def run_tests(self):
#        self.test_createTables()
#        self.test_dropTables()
        self.test_reset()
        pass
        
    def test_createTables(self):
        import os
        path = '../spring-uma-resource-server/src/main/resources/mysql/tables.yml'
        print os.listdir(os.path.dirname(path))
#        self.createTables(path)
#        self.createTables(path, how='print')
        self.createTables(path, how='on-server')

    def test_dropTables(self):
        path = '../spring-uma-resource-server/src/main/resources/mysql/tables.yml'
        self.dropTables(path, how='on-server')
#        with open(path) as stream:
#            tables = yaml.load(stream)
#            tables = tables.keys()
#            self.dropTables(tables)

    def test_reset(self):
        path = '../spring-uma-resource-server/src/main/resources/mysql/tables.yml'
        self.dropTables(path, how='on-server')
        self.createTables(path, how='on-server')
        
    
if __name__ == '__main__':
    tester = MySQLToolsTest()
    tester.run_tests()