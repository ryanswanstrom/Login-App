# Play Framework Login App

This app provides the basics of quickly setting up a Play Framework 1.2 web application.

# Features

  1. Login/Logout
  2. Registration Page
  1. Password Hashing implemented with [OWASP recommendations](https://www.owasp.org/index.php/Hashing_Java)
  1. robots.txt
  1. sitemap.xml
  1. HTML5
  1. Public User pages
  1. Private User pages
  1. Forgot Username
  1. Forgot Password - coming soon
  1. Easy to deploy on [Heroku](http://www.heroku.com)
  
# Getting Started

  1. git fork ...
  1. heroku create YourAppName
  1. Update **application.name** in the *conf/application.conf* file with Whatever your application name is
  1. Update the email settings in *conf/application.conf* with your SMTP server information
  1. heroku config:add ...
  1. git push heroku master
  
