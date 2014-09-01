Array.prototype.random = function() {
  return this[Math.floor(Math.random() * this.length)];
};

pastelColors = [
  '#CFE7E2',
  '#A6DEEE',
  '#B7B7FF',
  '#D881ED',
  '#FFA8FF',
  '#F0B9C8',
  '#FFB5B5',
  '#E9E9BE',
  '#E3D6AA',
  '#DDB791',
  '#DFB4A4',
  '#D69E87',
  '#E0BBA9',
  '#D7ACAC',
  '#FFD9B7',
  '#FFE099',
  '#FFF284',
  '#EEEEA2',
  '#ABFF73',
  '#A6CAA9',
  '#A3FEBA',
  '#8FFEDD',
  '#92FEF9',
  '#75ECFD',
  '#99C7FF',
  '#AAAAFF',
  '#C79BF2',
  '#E697E6',
  '#FFBBFF',
  '#FF9797',
  '#C4ABFE'
];

angular.module('utils', [])
.factory('$localstorage', ['$window', function($window) {
  return {
    set: function(key, value) {
      $window.localStorage[key] = value;
    },
    get: function(key, defaultValue) {
      return $window.localStorage[key] || defaultValue;
    },
    setObject: function(key, value) {
      $window.localStorage[key] = JSON.stringify(value);
    },
    getObject: function(key) {
      return JSON.parse($window.localStorage[key] || '{}');
    }
  }
}]);


var app = angular.module('MonMet', ['ionic', 'utils']);

changeColor = function() {
  var colors = ['orange', 'blue', 'green', 'purple', 'orange', 'blue', 'green', 'purple', 'orange', 'blue', 'green', 'purple'];
  var x = colors.random();
  $('#bus').removeClass('orange blue green purple');
  $('#bus').addClass(x + " fa fa-bus");
  setTimeout(changeColor, 2000);
}

app.run(function($ionicPlatform, $localstorage) {
  $ionicPlatform.ready(function() {
    // Hide the accessory bar by default (remove this to show the accessory bar above the keyboard
    // for form inputs)
    if(window.cordova && window.cordova.plugins.Keyboard) {
      cordova.plugins.Keyboard.hideKeyboardAccessoryBar(true);
    }

    if(window.StatusBar) {
      StatusBar.styleDefault();
    }

    //changeColor();
    // random title color
    /*
    var color = '';

    var titleColor = $localstorage.get('titleColor', '');
    if (titleColor === '') {
      color = colors.random();
    } else {
      var color = '';
      do {
        color = colors.random();
      } while (color === titleColor);
    }
    $('h1.title').addClass(color);
    $localstorage.set('titleColor', color);
    */
  });
});

computeNext3Rides = function(tt) {
  var now = new Date().getTime();
    var next = [];
    var day = new Date().getDay();
    var period = day == 0 ? tt.sunday : day == 6 ? tt.saturday : tt.week;
    for (var i = 0; i < period.length; ++i) {
      if (next.length > 2)
        break;

      var d = new Date();
      var t = period[i];
      var h = t.split(':')[0];
      var m = t.split(':')[1];
      d.setHours(parseInt(h));
      d.setMinutes(parseInt(m));

      if (d.getTime() > now) {
        var diff = parseInt((d.getTime() - now)/60000);
        next.push({
          min: diff,
          label: t,
          hurry: diff <= 8
        });
      }
    }

    return next;
};
