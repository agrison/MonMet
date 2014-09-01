app.controller('mainController', function($scope, $state, $localstorage) {
  $scope.toIndex = function(){
    $state.go('index');
  };

  $scope.toAbout = function() {
    $state.go('about');
  };

  $scope.toggleDelete = function() {
    $(".deleteButton").toggle();
    $(".nextRides").toggle();
  };

  $scope.GotoLink = function(link) {
    window.open(link, '_system');
  };

  $scope.currentTime = new Date();
})

.controller('indexController', function($scope, $state, $localstorage, $timeout) {
  $('#goBack').hide();
  $('#toggleDelete').show();

  $scope.favorites = $localstorage.getObject('favorites');
  $scope.noFavorites = function() {
    return Object.keys($scope.favorites).length == 0;
  }

  $scope.updateNextRides = function() {
    for (var i = 0; i < $scope.favorites.length; ++i) {
      if ($scope.favorites[i].timeTable) {
        var nextRides = computeNext3Rides($scope.favorites[i].timeTable);
        $scope.favorites[i]['next'] = _.first(nextRides, 3);
      }
    }
  };

  if ($scope.noFavorites()) {
    $localstorage.setObject('favorites', []); // init empty list
    $scope.favorites = $localstorage.getObject('favorites');
  }

  $scope.toAdd = function() {
    $state.go('add');
  }

  $scope.toViewStop = function(stop) {
    $state.go('viewStop');
  }

  $scope.viewFavorite = function(fav) {
    $localstorage.setObject('currentFavorite', fav);
    $state.go('view', {
      line: fav.lineId,
      head: fav.headId.split('|')[0],
      stop: fav.stopId
    });
  }

  $scope.deleteFavorite = function(fav) {
    $scope.favorites = _.without($scope.favorites, fav);
    $localstorage.setObject('favorites', $scope.favorites);
  }

  var cancelRefresh = null;
  $scope.updateNextRidesPeriodically = function() {
    cancelRefresh = $timeout(function myFunction() {
        $scope.currentTime = new Date();
        var interval = 10000;
        if ($scope.noFavorites()) {
          // cycle the bus through funky colors
          var colors = ['orange', 'blue', 'green', 'purple', 'orange', 'blue', 'green', 'purple', 'orange', 'blue', 'green', 'purple'];
          $('#bus').removeClass('orange blue green purple');
          $('#bus').addClass(colors.random() + " fa fa-bus");
          interval = 2500;
        } else {
          // refresh intervals
          $scope.updateNextRides();
        }

        cancelRefresh = $timeout(myFunction, interval);
    }, 1000);
  };

  $scope.$on('$destroy', function(e) {
        $timeout.cancel(cancelRefresh);
  });

  $scope.updateNextRidesPeriodically();
})

.controller('aboutController', function($scope, $state) {
  $('#goBack').show();
  $('#toggleDelete').hide();
})

.controller('addController', function($scope, $state, $localstorage) {
  $('#goBack').show();
  $('#toggleDelete').hide();

  $scope.selectedLine = 'woot';
  $scope.heads = [];
  $scope.selectedHead = '';
  $scope.stops = [];
  $scope.selectedStop = '';

  $scope.lineChange = function() {
    $.ajax({
      url: 'http://192.168.1.63:8080/api/lines/' + $scope.selectedLine.substr(1),
      type:'GET',
      success: function(data){
        $('#selectHead').prop('disabled', true);
        $scope.$apply(function() {
          $scope.heads = [];
          $scope.selectedHead = 'woot';
          console.log('----');
          console.log(data);
          for (var i = 0; i < data.length; ++i) {
            $scope.heads.push({value: data[i], name: data[i]});
          }
        });
        $('#selectHead').prop('disabled', false);
      }
    });
  };

  $scope.selectedHeads = null;
  $scope.allHeadsStops = {};
  $scope.matchingStops = [];

  $scope.headChange = function() {
    $.ajax({
      url: 'http://192.168.1.63:8080/api/lines/' + $scope.selectedLine.substr(1) + '/' + $scope.selectedHead,
      type:'GET',
      success: function(data) {
        $('#selectStop').prop('disabled', true);
        $scope.$apply(function() {
          $scope.stops = [];
          $scope.selectedStop = 'woot';
          for (var i = 0; i < data.length; ++i) {
            $scope.stops.push({value: data[i]['id'], name: data[i]['name']});
          }
        });
        $('#selectStop').prop('disabled', false);
      }
    });
  };

  $scope.cantAdd = function() {
    return $scope.selectedLine == 'woot' || $scope.selectedHead == 'woot' || $scope.selectedStop == 'woot';
  };

  var lineColor = function(line) {
    var lineColors = {
      'M': '#e67e22',
      'L': '#9b59b6',
      'C': '#3498db',
      'P': '#2ecc71',
      'N': '#8C6244'
    };
    return lineColors[line.substring(0, 1)];
  }

  $scope.add = function() {
    $.getJSON(
      'http://192.168.1.63:8080/api/tt/'
        + $scope.selectedLine.substr(1) + '/'
        + $scope.selectedHead.split('|')[0] + '/'
        + $scope.selectedStop,
      function(data) {
        $scope.$apply(function() {
          var favorites = $localstorage.getObject('favorites');
          favorites.push({
            line:   $('#selectLine option:selected').text(),
            lineId: $scope.selectedLine.substr(1),
            head:   $('#selectHead option:selected').text(),
            headId: $scope.selectedHead,
            stop:   $('#selectStop option:selected').text(),
            stopId: $scope.selectedStop,
            color: lineColor($scope.selectedLine) /*pastelColors.random()*/,
            timeTable: data,
            next: []
          });
          $localstorage.setObject('favorites', favorites);
          $state.go('index');
        });
      }
    );
  };
})

.controller('viewController', function($scope, $state, $localstorage) {
  $('#goBack').show();
  $('#toggleDelete').hide();
  $scope.current = $localstorage.getObject('currentFavorite');
  $scope.next = computeNext3Rides($scope.current.timeTable);

  $.ajax({
    url: 'http://lemet.fr/src/inc/LEMET_Cartographie.class.php?action=stop_info&arret=' + $scope.current.stop,
    dataType: 'jsonp',
    success: function(data) {
        var stop = _.find(data.stops,
             function(e) { return e.name == $scope.current.stop
                               && _.contains(e.lignes.split(','), $scope.current.line);} );
        var map = new L.Map('map').setView([stop.latlon[0], stop.latlon[1]], 17);
        L.tileLayer('http://{s}.mqcdn.com/tiles/1.0.0/osm/{z}/{x}/{y}.png',
                    {
                        maxZoom: 18,
                        attribution: 'Data, imagery and map information provided by <a href="http://open.mapquest.co.uk" target="_blank">MapQuest</a>, <a href="http://www.openstreetmap.org/" target="_blank">OpenStreetMap</a> and contributors.',
                        subdomains: ['otile1','otile2','otile3','otile4']
                    }
            ).addTo(map);

        L.marker([stop.latlon[0], stop.latlon[1]]).addTo(map)
              .bindPopup($scope.current.stop).openPopup();
    }
  });
});
