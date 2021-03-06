/**
 * Created with JetBrains WebStorm.
 * User: jack
 * Date: 13-6-10
 * Time: 下午2:50
 * To change this template use File | Settings | File Templates.
 */


Ext.define('CF.view.config.doorPanel' ,{
    extend: 'Ext.form.Panel',
    alias : 'widget.doorpanel', 
    frame: true,
    title: 'Company data',
    //bodyPadding: 5,
    //layout: 'fit',


    requires: [
        'CF.view.config.patternStatePanel'
    ],
    initComponent: function() {
        Ext.apply(this, {
        title: '地名匹配工具',

        items:[
            {
                xtype:'panel',
                layout: 'column',
                items: [
                    {
                        columnWidth: 0.5,
                        xtype: 'form',
                        margin: '10 10 10 10',
                        items:[
                            {

                                xtype: 'fieldset',
                                title:'空间数据',
                                layout: 'anchor',
                                defaultType: 'textfield',
                                items: [{
                                    fieldLabel: '数据库地址',
                                    value:'192.168.2.141:5432',
                                    anchor:'100%',
                                    name: 'host'
                                },{
                                    fieldLabel: '用户名',
                                    value:'postgres',
                                    anchor:'100%',
                                    name: 'user'
                                },{
                                    fieldLabel: '密码',
                                    anchor:'100%',
                                    value:'hvit',
                                    name: 'password'
                                },{
                                    fieldLabel: '数据库名',
                                    value:'xsdata20130719',
                                    anchor:'100%',
                                    name: 'dbname'
                                }, {
                                    xtype: 'radiogroup',
                                    fieldLabel: '数据库类型',
                                    columns: 2,
                                    defaults: {
                                        name: 'databsetype' //Each radio has the same name so the browser will make sure only one is checked at once
                                    },
                                    items: [{
                                        inputValue: '0',
                                        checked: true,
                                        boxLabel: 'postgres'
                                    }, {
                                        inputValue: '1',
                                        boxLabel: 'oracle'
                                    }]
                                },{
                                    xtype:'panel',
                                    layout: 'column',

                                    items:[
                                        {
                                            columnWidth: 0.6,
                                            fieldLabel: '表选择',
                                            name: 'proptable',
                                            anchor:'100%',
                                            xtype: 'combo',
                                            allowBlank: true,
                                            //blankText: "请选择",
                                            displayField: 'text',
                                            valueField: 'text',
                                            emptyText: '请选择表',
                                            disabled:true,
                                            listeners: {
                                                scope: this,
                                                'select': function (combo, records) {
                                                }
                                            },
                                            queryMode: 'local',
                                            flex: 1,
                                            store: Ext.widget('spacetables')

                                        },

                                        {
                                            columnWidth: 0.2,
                                            xtype:'button',
                                            text : '连接数据库',
                                            action:'connect'
                                        },

                                        {
                                            columnWidth: 0.2,
                                            xtype:'combo',
                                            name :'issplit',
                                            allowBlank: false,
                                            //fieldLabel : '是否分解',
                                            displayField: 'text',
                                            valueField: 'value',
                                            //defaultValue:false,
                                            value:false,
                                            forceSelection:true,
                                            queryMode: 'local',
                                            store: Ext.widget('issplitcombs')
                                        }

                                    ]

                                },
                                    ,{
                                        fieldLabel: '主键',
                                        value:'gid',
                                        anchor:'100%',
                                        name: 'mainkey'
                                    },{
                                    fieldLabel: '过滤条件',
                                    value:'where doorplate is not null',
                                    anchor:'100%',
                                    name: 'sql'
                                }]
                            }
                        ]
                    },{
                        columnWidth: 0.5,
                        xtype: 'form',
                        margin: '10 10 10 10',
                        items:[
                            {

                                xtype: 'fieldset',
                                title:'属性数据',
                                layout: 'anchor',
                                defaultType: 'textfield',
                                items: [{
                                    fieldLabel: '数据库地址',
                                    value:'192.168.2.141:1521:orcl',
                                    anchor:'100%',
                                    name: 'host'
                                },{
                                    fieldLabel: '用户名',
                                    value:'CIVILAFFAIRS_MZ',
                                    anchor:'100%',
                                    name: 'user'
                                },{
                                    fieldLabel: '密码',
                                    anchor:'100%',
                                    value:'hvit',
                                    name: 'password'
                                },{
                                    fieldLabel: '数据库名',
                                    value:'gnc',
                                    anchor:'100%',
                                    name: 'dbname'
                                }, {
                                    xtype: 'radiogroup',
                                    fieldLabel: '数据库类型',
                                    columns: 2,
                                    defaults: {
                                        name: 'databsetype' //Each radio has the same name so the browser will make sure only one is checked at once
                                    },
                                    items: [{
                                        inputValue: '0',

                                        boxLabel: 'postgres'
                                    }, {
                                        inputValue: '1',
                                        checked: true,
                                        boxLabel: 'oracle'
                                    }]
                                },{
                                    xtype:'panel',
                                    layout: 'column',

                                    items:[
                                        {
                                            columnWidth: 0.6,
                                            fieldLabel: '表选择',
                                            name: 'proptable',
                                            anchor:'100%',
                                            xtype: 'combo',
                                            allowBlank: true,
                                            //blankText: "请选择",
                                            displayField: 'text',
                                            valueField: 'text',
                                            emptyText: '请选择表',
                                            disabled:true,
                                            listeners: {
                                                scope: this,
                                                'select': function (combo, records) {
                                                }
                                            },
                                            queryMode: 'local',
                                            flex: 1,
                                            store: Ext.widget('spacetables')

                                        },

                                        {
                                            columnWidth: 0.2,
                                            xtype:'button',
                                            text : '连接数据库',
                                            action:'connect'
                                        },
                                        {
                                            columnWidth: 0.2,
                                            xtype:'combo',
                                            name :'issplit',
                                            allowBlank: false,
                                            //fieldLabel : '是否分解',
                                            displayField: 'text',
                                            valueField: 'value',
                                            //defaultValue:false,
                                            value:false,
                                            forceSelection:true,
                                            queryMode: 'local',
                                            store: Ext.widget('issplitcombs')
                                        }

                                    ]

                                }
                                    ,{
                                        fieldLabel: '主键',
                                        value:'id',
                                        anchor:'100%',
                                        name: 'mainkey'
                                    }
                                    ,{
                                        fieldLabel: '过滤条件',
                                        value:'where doorplate is not null',
                                        anchor:'100%',
                                        name: 'sql'
                                    }
                                ]
                            }
                        ]
                    }
                ]

            },
            {
               xtype:'patternstatepanel',
               height:250,

               title:'状态'
            }

        ],

         buttons: [
             {
                 xtype:"checkbox",
                 boxLabel  : '实时获取数据',
                 name      : 'topping',
                 inputValue: '2',
                 checked   : false,
                 id        : 'isgetRefreshData'
             },
            {
           text: '开始匹配',
           disabled:false,

           action:'beginpattern'

            }
                
		   ]
        });
        this.callParent(arguments);


        //store.loadPage(1);
        // store singleton selection model instance
    }
});
