#SimpleConnect

###About
The **SimpleConnect** library is a set of convinence libraries for performing asynchronous task, such as Http and Database calls, in Android.


###Getting Started
To get started download the `http-connect.jar` file from the project root directory and add it to build path and boom! Now you should be able to simple connect to the various android services supported in this library.

---

HTTPConnect
---
###Createing a new Request Object

To get started making a HTTP request you create a new `RequestMaker` object. The object requires an application context so it must be called from an Android activity type class.

```java
RequestMaker http = new Requestmaker(this);
```

Now that you have a request object, are now free to make `get`, `put`, `post` and `delete` request. This single instance of the `RequestMaker` object can be freely passed around your application or you can make as many instances of the object as you require.

**Hint: It is advised that you reuse the same RequestMaker object as much as possible to avoid garbage collection.*

---

###Making a HTTP GET Request  
Usimg the ``http`` object created earlier you can perform a http ``get`` request to any url by invoking the get method of the http object and passing as the first parameter the url u wish to get from, and a new ``HttpResponder`` object that can be passed anonymously as shown below:

```java
http.get("http://www.example.com", new HttpResponder() {
    ...
});

```

**Hint: The HttpReponder can be passed as `null` if you wish not to handle the response.*

The HttpResponder is your callback function that receives the http response. This object has one method that you must implement to handled to the server reponse. This reponse is wrapped in a `ServerReponse` object which has methods you can use to retrieve the server status codes, message and response content.
```java
    @Override
    public void onHttpResponse(ServerResponse httpResponse) {
        // handle the http responses here
    }
```

**Hint:  Any Android error, such as WiFi connection errors, will throw an ``AndroidErrorException``. Be sure to handle any exception that might be thrown using try-catch statements.*

**Hint: The http response body must only contatin string data as the ServerResponse object is only able to replicate string type data (i.e. sending html, xml, or json should work as expected.*


---
##Making a HTTP POST/PUT Request  
When making a `post/put` request you use the respective method of the resquest object. The first parameter is the url, the second parameter is a ``json`` string describing the request body and the finial parameter is the ``HttpResponder`` as shown below:
```java
http.get("http://www.example.com", "{\"key\":\"value\"}" , new HttpResponder() {
    ...
});
```
**Hint: This library only supports put and post request to json consuming servers. The http content type is set to appliction/json by default and cannot be changed as of this version.*

---
###Making a HTTP DELETE Request
The `delete` request works by calling the delete method on the `RequestMaker` object, passing the url as the first parameter and a `HttpResponder` as the second paramter as shown:

```java
http.delete("http://www.example.com", new HttpResponder() {
    ...
});

```
Alternativly, you can pass a json string as the second parameter and the HttpResponder as a third if you wish to pass some data to the server. See below:
```java
http.delete("http://www.example.com", "{\"key\":\"value\"}" , new HttpResponder() {
    ...
});
```
