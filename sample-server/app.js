var express = require('express');
var http = require('http');
var path = require('path');
var favicon = require('static-favicon');
var logger = require('morgan');
var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser');

var app = express();

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'jade');

app.use(favicon());
app.use(logger('dev'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded());
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));
app.use(app.router);



app.get('/', function (req, res){
    res.send("Server got your message and sends the following reply \"Hello. :D\"");
});

app.put('/', function (req, res){
    res.send("Woow.... Where do you want me to put this - " + JSON.stringify(req.body));
});

app.post('/', function (req, res){
    res.send("Woow.... Where do you want me to post this - " + JSON.stringify(req.body));
});

app.delete('/:id', function (req, res){
    res.send("Could not find and delete the following - " + req.param('id'));
});


// /// catch 404 and forwarding to error handler
// app.use(function(req, res, next) {
//     var err = new Error('Not Found');
//     err.status = 404;
//     next(err);
// });

// /// error handlers

// // development error handler
// // will print stacktrace
// if (app.get('env') === 'development') {
//     app.use(function(err, req, res, next) {
//         res.render('error', {
//             message: err.message,
//             error: err
//         });
//     });
// }

// // production error handler
// // no stacktraces leaked to user
// app.use(function(err, req, res, next) {
//     res.render('error', {
//         message: err.message,
//         error: {}
//     });
// });


//module.exports = app;

app.listen(2000, function (){console.log("Server started...")})
