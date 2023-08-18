import FileSaver from 'file-saver';
import * as XLSX from 'xlsx';
import xlsxStyle from 'xlsx-style';
/**
 * 通用js方法封装处理
 * Copyright (c) 2019 ruoyi
 */

// 日期格式化
export function parseTime(time, pattern) {
	if (arguments.length === 0 || !time) {
		return null
	}
	const format = pattern || '{y}-{m}-{d} {h}:{i}:{s}'
	let date
	if (typeof time === 'object') {
		date = time
	} else {
		if ((typeof time === 'string') && (/^[0-9]+$/.test(time))) {
			time = parseInt(time)
		} else if (typeof time === 'string') {
			time = time.replace(new RegExp(/-/gm), '/').replace('T', ' ').replace(new RegExp(/\.[\d]{3}/gm),'');
		}
		if ((typeof time === 'number') && (time.toString().length === 10)) {
			time = time * 1000
		}
		date = new Date(time)
	}
	const formatObj = {
		y: date.getFullYear(),
		m: date.getMonth() + 1,
		d: date.getDate(),
		h: date.getHours(),
		i: date.getMinutes(),
		s: date.getSeconds(),
		a: date.getDay()
	}
	const time_str = format.replace(/{(y|m|d|h|i|s|a)+}/g, (result, key) => {
		let value = formatObj[key]
		// Note: getDay() returns 0 on Sunday
		if (key === 'a') { return ['日', '一', '二', '三', '四', '五', '六'][value] }
		if (result.length > 0 && value < 10) {
			value = '0' + value
		}
		return value || 0
	})
	return time_str
}

// 表单重置
export function resetForm(refName) {
	if (this.$refs[refName]) {
		this.$refs[refName].resetFields();
	}
}

// 添加日期范围
export function addDateRange(params, dateRange, propName) {
	let search = params;
	search.params = typeof (search.params) === 'object' && search.params !== null && !Array.isArray(search.params) ? search.params : {};
	dateRange = Array.isArray(dateRange) ? dateRange : [];
	if (typeof (propName) === 'undefined') {
		search.params['beginTime'] = dateRange[0];
		search.params['endTime'] = dateRange[1];
	} else {
		search.params['begin' + propName] = dateRange[0];
		search.params['end' + propName] = dateRange[1];
	}
	return search;
}

// 回显数据字典
export function selectDictLabel(datas, value) {
	var actions = [];
	Object.keys(datas).some((key) => {
		if (datas[key].dictValue == ('' + value)) {
			actions.push(datas[key].dictLabel);
			return true;
		}
	})
	return actions.join('');
}

// 回显数据字典（字符串数组）
export function selectDictLabels(datas, value, separator) {
	var actions = [];
	var currentSeparator = undefined === separator ? "," : separator;
	var temp = value.split(currentSeparator);
	Object.keys(value.split(currentSeparator)).some((val) => {
		Object.keys(datas).some((key) => {
			if (datas[key].value == ('' + temp[val])) {
				actions.push(datas[key].label + currentSeparator);
			}
		})
	})
	return actions.join('').substring(0, actions.join('').length - 1);
}

// 字符串格式化(%s )
export function sprintf(str) {
	var args = arguments, flag = true, i = 1;
	str = str.replace(/%s/g, function () {
		var arg = args[i++];
		if (typeof arg === 'undefined') {
			flag = false;
			return '';
		}
		return arg;
	});
	return flag ? str : '';
}

// 转换字符串，undefined,null等转化为""
export function praseStrEmpty(str) {
	if (!str || str == "undefined" || str == "null") {
		return "";
	}
	return str;
}

// 数据合并
export function mergeRecursive(source, target) {
    for (var p in target) {
        try {
            if (target[p].constructor == Object) {
                source[p] = mergeRecursive(source[p], target[p]);
            } else {
                source[p] = target[p];
            }
        } catch(e) {
            source[p] = target[p];
        }
    }
    return source;
};

/**
 * 构造树型结构数据
 * @param {*} data 数据源
 * @param {*} id id字段 默认 'id'
 * @param {*} parentId 父节点字段 默认 'parentId'
 * @param {*} children 孩子节点字段 默认 'children'
 */
export function handleTree(data, id, parentId, children) {
	let config = {
		id: id || 'id',
		parentId: parentId || 'parentId',
		childrenList: children || 'children'
	};

	var childrenListMap = {};
	var nodeIds = {};
	var tree = [];

	for (let d of data) {
		let parentId = d[config.parentId];
		if (childrenListMap[parentId] == null) {
			childrenListMap[parentId] = [];
		}
		nodeIds[d[config.id]] = d;
		childrenListMap[parentId].push(d);
	}

	for (let d of data) {
		let parentId = d[config.parentId];
		if (nodeIds[parentId] == null) {
			tree.push(d);
		}
	}

	for (let t of tree) {
		adaptToChildrenList(t);
	}

	function adaptToChildrenList(o) {
		if (childrenListMap[o[config.id]] !== null) {
			o[config.childrenList] = childrenListMap[o[config.id]];
		}
		if (o[config.childrenList]) {
			for (let c of o[config.childrenList]) {
				adaptToChildrenList(c);
			}
		}
	}
	return tree;
}


//说明：javascript的乘法结果会有误差，在两个浮点数相乘的时候会比较明显。这个函数返回较为精确的乘法结果。
//调用：accMul(arg1,arg2)
//返回值：arg1乘以 arg2的精确结果
export function numberMul(arg1, arg2) {
	if(arg1 == null || arg2 == null){
	  return null;
	}
	var m = 0, s1 = arg1.toString(), s2 = arg2.toString();
	try {
	  m += s1.split(".")[1].length
	} catch (e) {
	}
	try {
	  m += s2.split(".")[1].length
	} catch (e) {
	}
	return Number(s1.replace(".", "")) * Number(s2.replace(".", "")) / Math.pow(10, m)
  }



  /**
 * 导出成xlsx格式的excel
 * @author JiaBaoYu
 * @date 2022/08/08
 * @param dom 要导出的dom元素
 * @param name 导出文件名称（不含.xlsx)
 */
export function exportDomToXlsx(dom, name) {
	let table_book;
	let fix = document.querySelector('.el-table__fixed');
	if(fix){ //判断要导出的节点中是否有fixed的表格，如果有，转换excel时先将该dom移除，然后append回去
		console.log("fixfixfixfixfixfixfixfix")
		table_book = XLSX.utils.table_to_sheet(dom.removeChild(fix));
		dom.appendChild(fix);
		console.log(dom,"domdomdomdomdomdomdomdom")
		console.log(table_book,"table_booktable_booktable_booktable_booktable_book")
	}else{
		table_book = XLSX.utils.table_to_sheet(dom,{ raw: true });
	}
	addRangeBorder(table_book['!merges'],table_book)//为导出模板的合并表头项添加边框
	//创建一个workbook对象
	let wb = XLSX.utils.book_new()
	//把worksheet对象添加进workbook对象，第三个参数是excel中sheet的名字
	XLSX.utils.book_append_sheet(wb, table_book, name)
	setExlStyle(wb['Sheets'][name]); // 设置列宽 字号等 如果无需多余的样式则省略
	const table_write = xlsxStyle.write(wb, { type: 'buffer'})
	try {
		FileSaver.saveAs(
		new Blob([table_write], { type: 'application/octet-stream' }),
		name + '.xlsx'
		)
	} catch (e) {
		console.log(e, table_write)
	}
	return table_write;
}



//为合并项添加边框
export function addRangeBorder(range,ws){
   let cols = ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
			"AA", "AB", "AC", "AD", "AE", "AF", "AG", "AH", "AI", "AJ", "AK", "AL", "AM", "AN", "AO", "AP", "AQ", "AR", "AS", "AT", "AU", "AV", "AW", "AX", "AY", "AZ",
			"BA", "BB", "BC", "BD", "BE", "BF", "BG", "BH", "BI", "BJ", "BK", "BL", "BM", "BN", "BO", "BP", "BQ", "BR", "BS", "BT", "BU", "BV", "BW", "BX", "BY", "BZ",
			"CA", "CB", "CC", "CD", "CE", "CF", "CG", "CH", "CI", "CJ", "CK", "CL", "CM", "CN", "CO", "CP", "CQ", "CR", "CS", "CT", "CU", "CV", "CW", "CX", "CY", "CZ",
			"DA", "DB", "DC", "DD", "DE", "DF", "DG", "DH", "DI", "DJ", "DK", "DL", "DM", "DN", "DO", "DP", "DQ", "DR", "DS", "DT", "DU", "DV", "DW", "DX", "DY", "DZ",
			"EA", "EB", "EC", "ED", "EE", "EF", "EG", "EH", "EI", "EJ", "EK", "EL", "EM", "EN", "EO", "EP", "EQ", "ER", "ES", "ET", "EU", "EV", "EW", "EX", "EY", "EZ",
			"FA", "FB", "FC", "FD", "FE", "FF", "FG", "FH", "FI", "FJ", "FK", "FL", "FM", "FN", "FO", "FP", "FQ", "FR", "FS", "FT", "FU", "FV", "FW", "FX", "FY", "FZ",
			"GA", "GB", "GC", "GD", "GE", "GF", "GG", "GH", "GI", "GJ", "GK", "GL", "GM", "GN", "GO", "GP", "GQ", "GR", "GS", "GT", "GU", "GV", "GW", "GX", "GY", "GZ",
			"HA", "HB", "HC", "HD", "HE", "HF", "HG", "HH", "HI", "HJ", "HK", "HL", "HM", "HN", "HO", "HP", "HQ", "HR", "HS", "HT", "HU", "HV", "HW", "HX", "HY", "HZ",
			"IA", "IB", "IC", "ID", "IE", "IF", "IG", "IH", "II", "IJ", "IK", "IL", "IM", "IN", "IO", "IP", "IQ", "IR", "IS", "IT", "IU", "IV", "IW", "IX", "IY", "IZ",
			"JA", "JB", "JC", "JD", "JE", "JF", "JG", "JH", "JI", "JJ", "JK", "JL", "JM", "JN", "JO", "JP", "JQ", "JR", "JS", "JT", "JU", "JV", "JW", "JX", "JY", "JZ",
			"KA", "KB", "KC", "KD", "KE", "KF", "KG", "KH", "KI", "KJ", "KK", "KL", "KM", "KN", "KO", "KP", "KQ", "KR", "KS", "KT", "KU", "KV", "KW", "KX", "KY", "KZ",
			];
  range.forEach(item => {
	let style = {
	  s: {
		border: {
		  top: { style: 'thin' },
		  left: { style: 'thin' },
		  bottom: { style: 'thin' },
		  right: { style: 'thin' }
		}
	  }
	}
   // 处理合并行
	for (let i = item.s.c; i <= item.e.c; i++) {
	  ws[`${cols[i]}${Number(item.e.r) + 1}`] = ws[`${cols[i]}${Number(item.e.r) + 1}`] || style
	  // 处理合并列
	  for (let k = item.s.r + 2; k <= item.e.r + 1; k++) {
		ws[cols[i] + k] = ws[cols[k] + item.e.r] || style
	  }
	}
  })
  return ws;
}


//设置单元格样式（列宽 字号等）
export function setExlStyle(data) {
  let borderAll = {  //单元格外侧框线
	top: {
	  style: 'thin',
	},
	bottom: {
	  style: 'thin'
	},
	left: {
	  style: 'thin'
	},
	right: {
	  style: 'thin'
	}
  };
  data['!cols'] = [];
  for (let key in data) {
	console.log(key)
	if (data[key] instanceof Object) {
	  data[key].s = {
		border: borderAll,
		alignment: {
		  horizontal: 'center',   //水平居中对齐
		  vertical:'center',
		  wrapText: true 
		},
		font:{
		  sz:11
		},
		bold:true,
		numFmt: 0
	  }
	  data['!cols'].push({wpx: 100});//可自行根据需求设置列宽，这里设置为统一列宽
	}
  }
  return data;
}