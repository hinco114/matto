var express = require('express');
var app = express();

var session = require('express-session');
var path = require('path');
var favicon = require('serve-favicon');
var logger = require('morgan');
var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser');
//Login Authenticate
var passport = require('passport');
//var passport_config = require('./routes/passport');
var memberService = require('./routes/memberService');
var productService = require('./routes/productService');
var toiletService = require('./routes/toiletService');
var toiletProductService = require('./routes/toiletProductService');

app.use(logger('dev'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));
app.use(cookieParser());


//passport,session module

app.use(session({
    secret : 'Secret Key',
    resave : false,
    saveUninitialized:true
}));
app.use(passport.initialize());
app.use(passport.session());
//passport_config();

//front-end resource static path
app.use(express.static(path.join(__dirname, 'public')));


app.use('/api/0.1v',memberService);
app.use('/api/0.1v',productService);
app.use('/api/0.1v',toiletProductService);
app.use('/api/0.1v',toiletService);


app.get('/',function(req,res){
   res.end('SERVER TEST'); 
});
app.listen(3000);