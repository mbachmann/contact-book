# Spring-Contact-Book

[https://github.com/mbachmann/contact-book](https://github.com/mbachmann/contact-book)

A simple contact book application.

Original Repository: [https://github.com/andreas-octavianus/Spring-Contact-Book](https://github.com/andreas-octavianus/Spring-Contact-Book)

## Learn Target

How to create a ReST-API application using Spring-Boot Framework.

    Spring-Boot
    Spring-Web
    Spring-Data

## Configuration

#### Database

    H2 Database

Hibernate generates database tables automatically by run this application and deletes table by terminate this application.

#### Server

    Port: 8080

#### Edit Default Configuration

Find application.yml in resources folder and edit configuration in this file.

## API

    contacts:       {host:port}/contacts
    contact by id:  {host:port}/contacts/{id}
    new contact:   {host:port}/contacts/new
    edit contact:   {host:port}/contacts/edit/{id}
    delete contact: {host:port}/contacts/{id}/delete

## JSON

Data folder contains a sample JSON-file to save contact.

##  Create a Docker Container, Run and Publish to Docker

Create first a jar with the build instruction. To create a container. Replace **uportal** with your **dockerhub id**.

<br/>

```

$  docker build -t uportal/contact-book .
$  docker run -p 8080:8080 --rm -it  uportal/contact-book
```

<br/>

_Ctrl c_ will stop and delete the container.

<br/>

Replace **uportal** with your **dockerhub id**.

<br/>

```
$  docker login
$  docker login --username uportal --password 
$  docker push uportal/contact-book
```
<br/>

Login to deployment platform with a container infrastructure:

<br/>

Replace **uportal** with your **dockerhub id**.

<br/>

```
$  docker pull uportal/contact-book
```

<br/>

###  docker-compose

Start the files with:

<br/>

Start with log output in the console:

```
$  docker-compose -f docker-compose-h2.yml up
```

<br/>

Start in detached mode

```
$  docker-compose -f docker-compose-h2.yml up -d
```

<br/>

Delete containers:

```
$  docker-compose -f docker-compose-h2.yml rm
```

<br/>

### Create a Dockerfile

The Dockerfile takes a slim _JDK11 image_, adds the _todo-*.jar_ file from the _target_ folder with the name _app.jar_. This jar file is started by _java -jar_ command.

<br/>

