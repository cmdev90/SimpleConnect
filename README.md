#Simple Connect

###Usage
Download the ``http-connect.jar`` file from the project root directory and add it to build path and boom! Now you should be able to simple connect to various android services. Read the rest of the this documentation to find out more about utilizing this convenience library.

---

HTTPConnect
---
###Getting Started

To get started making a HTTP request you create a new ```RequestMaker``` object. The object requires an application context so it must be called from an Android activity type class.

```java
RequestMaker http = new Requestmaker(this);
```

Now that you have a request object, are now free to make ``get``, ``put``, ``post`` and ``delete`` request. This single instance of the ``RequestMaker`` object can be freely passed around your application or you can make as many instances of the object as you require. It is recommended to try an reuse the object as much as possible and limit it to one instance per ``Activity`` class.

---

###Making a HTTP Request  
As an example we will show you how to use the ``http`` object we created earlier by making a ``get`` request to http://www.google.com. What we expect to get back from this response would me a mess of HTML mark up that builds Google's home page. To do this we simply pass the website address and a new ``HttpResponder``, and implement the onHttpResponse method which will serve as the callback function for this call.

```java
http.get("http://www.google.com", new HttpResponder() {

    @Override
    public void onHttpResponse(ServerResponse httpResponse) {
        // handle the http responses here
    }

});
```
The callback function ``onHttpResponse`` returns a ``ServerResponse`` object which contains all the necessary details of the server response. Any Android error, such as WiFi connection errors, will throw an ``AndroidErrorException`` or something like that. Be sure to handle any exception that might be thrown.

