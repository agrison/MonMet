<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Mon Met'</title>

    <link href="http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css" rel="stylesheet">

    <link href='http://fonts.googleapis.com/css?family=Lato:300,400,900' rel='stylesheet' type='text/css'>
    <link href="http://maxcdn.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css" rel="stylesheet">

    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
    <script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
    <![endif]-->

    <style type="text/css">
        body {
            background-color: #f2f2f2;
            font-family: 'Lato', sans-serif;
            font-weight: 300;
            font-size: 16px;
            color: #555;

            -webkit-font-smoothing: antialiased;
            -webkit-overflow-scrolling: touch;
        }

        h1, h2, h3, h4, h5, h6 {
            font-family: 'Lato', sans-serif;
            font-weight: 300;
            color: #333;
        }

        h1 {
            font-size: 40px;
        }

        h3 {
            color: #95a5a6;
            font-weight: 400;
        }

        h4 {
            color: #95a5a6;
            font-weight: 400;
            font-size: 20px;
        }

        p {
            line-height: 28px;
            margin-bottom: 25px;
            font-size: 16px;
        }

        .centered {
            text-align: center;
        }

        a {
            color: #2c3e50;
            word-wrap: break-word;

            -webkit-transition: color 0.1s ease-in, background 0.1s ease-in;
            -moz-transition: color 0.1s ease-in, background 0.1s ease-in;
            -ms-transition: color 0.1s ease-in, background 0.1s ease-in;
            -o-transition: color 0.1s ease-in, background 0.1s ease-in;
            transition: color 0.1s ease-in, background 0.1s ease-in;
        }

        a:hover,
        a:focus {
            color: #7b7b7b;
            text-decoration: none;
            outline: 0;
        }

        a:before,
        a:after {
            -webkit-transition: color 0.1s ease-in, background 0.1s ease-in;
            -moz-transition: color 0.1s ease-in, background 0.1s ease-in;
            -ms-transition: color 0.1s ease-in, background 0.1s ease-in;
            -o-transition: color 0.1s ease-in, background 0.1s ease-in;
            transition: color 0.1s ease-in, background 0.1s ease-in;
        }

        hr {
            display: block;
            height: 1px;
            border: 0;
            border-top: 1px solid #ccc;
            margin: 1em 0;
            padding: 0;
        }

        .navbar-default {
            background-color: #2c3e50;
            border-color: transparent;
        }

        .navbar-default .navbar-brand {
            color: white;
        }

        .navbar-default .navbar-nav > li > a {
            color: white;
        }

        .mt {
            margin-top: 40px;
            margin-bottom: 40px;
        }

        .form-control {
            height: 42px;
            font-size: 18px;
            width: 280px;
        }

        i {
            margin: 8px;
            color: #2c3e50;
        }

        #headerwrap {
            background-color: #2c3e50;
            margin-top: -20px;
            padding-top: 200px;
            background-attachment: relative;
            background-position: center center;
            min-height: 650px;
            width: 100%;

            -webkit-background-size: 100%;
            -moz-background-size: 100%;
            -o-background-size: 100%;
            background-size: 100%;

            -webkit-background-size: cover;
            -moz-background-size: cover;
            -o-background-size: cover;
            background-size: cover;
        }

        #headerwrap h1 {
            margin-top: 60px;
            margin-bottom: 15px;
            color: white;
            font-size: 45px;
            font-weight: 300;
            letter-spacing: 1px;
        }
    </style>
</head>

<body>

<!-- Fixed navbar -->
<div class="navbar navbar-default navbar-fixed-top">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="#"><b>Mon Met'</b></a>
        </div>
        <div class="navbar-collapse collapse">
            <ul class="nav navbar-nav navbar-right">
                <li><a href="#">${hits} API hits</a></li>
            </ul>
        </div>
    </div>
</div>

<div id="headerwrap">
    <div class="container">
        <div class="row">
            <div class="col-lg-6">
                <h1>Mon Met', mon compagnon du réseau Met'.</h1>

                <div style="text-align: center">
                    <a class="btn btn-lg" style="color: #ecf0f1; background-color: #95a5a6; margin: 20px"><i
                            class="fa fa-apple" style="color: #ecf0f1"></i> Apple Store</a>
                    <a class="btn btn-lg" style="color: #ecf0f1; background-color: #95a5a6; margin: 20px"><i
                            class="fa fa-google" style="color: #ecf0f1"></i> Goole Play</a></p>
                </div>
            </div>
            <div class="col-lg-6">
                <img class="img-responsive" src="pic2.png" style="margin-top: -100px" alt="">
            </div>

        </div>
    </div>
</div>


<div class="container">
    <div class="row mt centered">
        <div class="col-lg-9 col-lg-offset-2">
            <h1>Vos arrêts à porté de main, <br/>Les 3 prochains bus en un coup d'oeil.</h1>

            <h3>Mon Met' vous permet de sauvegarder les horaires de vos arrêts de bus favoris sur votre téléphone et
                de les consulter à n'importe quel moment.</h3>
        </div>
    </div>

    <div class="row mt centered">
        <div class="col-lg-4">
            <i class="fa fa-download" style="font-size: 11em; color: #2980b9; text-shadow: 1px 1px 1px gray"></i>
            <h4>1 - Téléchargez l'application</h4>

            <p>Téléchargez l'application sur votre store.<br/>
                <a type="submit" class="btn btn-lg" style="color: #ecf0f1; background-color: #2c3e50"><i
                        class="fa fa-apple" style="color: #ecf0f1"></i> Apple Store</a>
                <a type="submit" class="btn btn-lg" style="color: #ecf0f1; background-color: #2c3e50"><i
                        class="fa fa-google" style="color: #ecf0f1"></i> Goole Play</a></p>
        </div>

        <div class="col-lg-4">
            <i class="fa fa-bus" style="font-size: 11em; color: #8e44ad; text-shadow: 1px 1px 1px gray"></i>
            <h4>2 - Ajoutez vos arrêts</h4>

            <p>Ajoutez vos arrêts favoris à votre téléphone, en cherchant par ligne et direction.</p>
        </div>

        <div class="col-lg-4">
            <i class="fa fa-clock-o" style="font-size: 11em; color: #d35400; text-shadow: 1px 1px 1px gray"></i>
            <h4>3 - Profitez</h4>

            <p>Les horaires de vos arrêts sont consultables facilement, les 3 prochains passages visibles d'un coup
                d'oeil.</p>
        </div>
    </div>
    <hr>
</div>

<div class="container">
    <div class="row mt centered">
        <div class="col-lg-6 col-lg-offset-3">
            <h1>Mon Met' pour les développeurs.</h1>

            <h3>Cette application est gratuite et open-source, rendez-vous sur Github.</h3>
            <a href="https://github.com/agrison/MonMet"><i class="fa fa-github" style="font-size: 7em"></i></a>
        </div>
    </div>
</div>

<div class="container">
    <hr>
    <p class="centered">Created by Alexandre Grison - dageeks.com - 2014</p>
</div>

<script src="https://code.jquery.com/jquery-1.10.2.min.js"></script>
<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
</body>
</html>
