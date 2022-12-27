  var powerType = [{
      value: '0',
      label: '有功功率',
      unit: 'kW',
      dataItem: [{
          name: 'A相',
          code: '178',
          key:'Pa'
        },
        {
          name: 'B相',
          code: '179',
          key:'Pb'
        },
        {
          name: 'C相',
          code: '180',
          key:'Pc'
        },
        {
          name: '总有功功率',
          code: 'P',
          key:'P'
        },
      ]
    },
    {
      value: '1',
      label: '电流',
      unit: 'A',
      dataItem: [{
          name: 'A相',
          code: '172',
          key:'Ia'
        },
        {
          name: 'B相',
          code: '173',
          key:'Ib'
        },
        {
          name: 'C相',
          code: '174',
          key:'Ic'
        },
      ]
    },
    {
      value: '2',
      label: '相电压',
      unit: 'V',
      dataItem: [{
        name: 'A相',
        code: '172',
        key:'Ua'
      },
      {
        name: 'B相',
        code: '173',
        key:'Ub'
      },
      {
        name: 'C相',
        code: '174',
        key:'Uc'
      },
    ]
    },
    {
      value: '3',
      label: '线电压',
      unit: 'V',
      dataItem: [{
        name: 'AB线',
        code: '172',
        key:'Uab'
      },
      {
        name: 'BC线',
        code: '173',
        key:'Ubc'
      },
      {
        name: 'CA线',
        code: '174',
        key:'Uca'
      },
    ]
    },
    {
      value: '4',
      label: '频率',
      unit: 'Hz',
      dataItem: [{
        name: '频率',
        code: '191',
        key:'Fr'
      }]
    },
    {
      value: '5',
      label: '功率因数',
      unit: '',
      dataItem: [{
        name: '功率因数',
        code: '189',
        key:'cos'
      }]
    },
    {
      value: '6',
      label: '无功功率',
      unit: 'kVar',
      dataItem: [{
        name: 'A相',
        code: '190',
        key:'Qa'
      },{
        name: 'B相',
        code: 'Qb',
        key:'cos'
      },{
        name: 'C相',
        code: '190',
        key:'Qc'
      },{
        name: '总无功功率',
        code: '190',
        key:'Q'
      }]
    },
    {
      value: '7',
      label: '视在功率',
      unit: 'kVA',
      dataItem: [{
        name: '视在功率',
        code: '213',
        key:'S'
      }]
    },
    // {
    //   value: '8',
    //   label: '三相不平衡度',
    //   unit: '%',
    //   dataItem: [{
    //     name: '三相不平衡度',
    //     code: '01'
    //   }]
    // },
    // {
    //   value: '9',
    //   label: '负载率',
    //   unit: '%',
    //   dataItem: [{
    //     name: '负载率',
    //     code: '201'
    //   }]
    // }
  ]
  export {
    powerType
  }
