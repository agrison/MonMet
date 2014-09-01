app.config(function($stateProvider, $urlRouterProvider) {
    $stateProvider
      .state('index', {
        url: '/',
        templateUrl: 'partials/index.tpl',
        controller: 'indexController'
      })
      .state('about', {
        url: '/about',
        templateUrl: 'partials/about.tpl',
        controller: 'aboutController'
      })
      .state('add', {
        url: '/add',
        templateUrl: 'partials/add.tpl',
        controller: 'addController'
      })
      .state('view', {
        url: '/view/{line}/{head}/{stop}',
        templateUrl: 'partials/view.tpl',
        controller: 'viewController'
      });

    $urlRouterProvider.otherwise('/');
});
