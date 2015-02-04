<%@attribute name="pageName" required="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="o" tagdir="/WEB-INF/tags"%>

    <nav class="navbar navbar-inverse navbar-fixed-top">
      <div class="container">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="#"> <strong> UMA Resource Server </strong> </a>
        </div>
        <div id="navbar" class="collapse navbar-collapse">
          <ul class="nav navbar-nav navbar-right btn-custom">
            <li id="home-page"><a href="#">Home</a></li>
            <li class="divider-vertical"></li>
            <li id="login-page"><a href="/login">Login</a></li>
            <li class="divider-vertical"></li>
            <li id="register-page"><a href="/register">Register</a></li>
            <li class="divider-vertical"></li>            
            <li id="about-page"><a href="#about">About</a></li>
            <li class="divider-vertical"></li>
            <li id="contact-page"><a href="#contact">Contact</a></li>
          </ul>
        </div><!--/.nav-collapse -->
      </div>
    </nav>	