/**
 * Ext.Loader
 */
Ext.Loader.setConfig({
    enabled: true,
    disableCaching: false,
    paths: {
        GeoExt: "js/geoext4/src/GeoExt",
        // for dev use
        Ext: "http://192.168.2.112/ext-4.2.1/src"
        // for build purpose
        //Ext: "extjs-4.1.0/src"
    }
});


/**
 * CF.app
 * A MVC application demo that uses GeoExt and Ext components to display
 * geospatial data.
 */
Ext.application({
    name: 'CF',
    appFolder: 'js/app',
    controllers: [
        'Map' ,'Navigation','Config'
    ],
    autoCreateViewport: true
});

/**
 * For dev purpose only
 */
var ctrl, map, mapPanel;
