var baseUrl = 'http://127.0.0.1';

//全局默认配置
axios.defaults.baseURL = baseUrl;

$('#tab').bootstrapTable({
    method : 'get',
    url : baseUrl+"/scheduleJob/findList",//请求路径
    striped : true, //是否显示行间隔色
    pageNumber : 1, //初始化加载第一页
    pagination : true,//是否分页
    sidePagination : 'client',//server:服务器端分页|client：前端分页
    pageSize : 5,//单页记录数
    pageList : [ 5, 10, 20, 30 ],//可选择单页记录数
    showRefresh : false,//刷新按钮
    queryParams : function(params) {//上传服务器的参数
        var temp = {//如果是在服务器端实现分页，limit、offset这两个参数是必须的
            limit : params.limit, // 每页显示数量
            offset : params.offset, // SQL语句起始索引
            title : $('#title').val(),
            className : $('#className').val()
        };
        return temp;
    },
    columns : [
        {title : 'id', field : 'id',visible:false},
        {title : '标题', field : 'title', align:'center',sortable : true},
        {title : 'className', field : 'className',align:'center'},
        {title : 'cron表达式', field : 'cron',align:'center'},
        {title : '描述', field : 'description',align:'center'},
        {title : '启用状态', field : 'status',align:'center', formatter : formatEnum,cellStyle:cellStyle},
        {title : '运行状态', field : 'runStatus',align:'center',formatter : formatEnum,cellStyle:cellStyle},
        {title : '下一次执行时间', field : 'nextExeTime',align:'center'},
        {title : '最后一次执行时间', field : 'lastExeTime',align:'center'},
        {title : '最后一次业务执行状态', field : 'lastBusinessStatus',align:'center', formatter : formatEnum,cellStyle:cellStyle},
        {title : '操作', field : 'id',align:'center', formatter : operation}
    ]
});

//value代表该列的值，row代表当前对象
function formatEnum(value, row, index) {
    return value.name;
}

//单元格样式
function cellStyle(value, row, index) {
    if (value.value==1) {
        return {classes:'statusStyle1'}
    }
    return {classes:'statusStyle0'}
}

//删除、编辑操作
function operation(value, row, index) {
    var htm ='';
    if (row.runStatus.value==1) {
        htm += '<button type="button"  class="opt-btn btn btn-danger btn-sm" onclick="stop('+row.id+')">暂停</button>';
    }else {
        htm += '<button type="button"  class="opt-btn btn btn-warning btn-sm" onclick="start('+row.id+')">开始</button>';
    }
    /*  htm += '<button type="button" class="btn btn-danger">启用</button>';*/
    htm += '<button type="button" style="margin: 0 5px 0 5px" class="btn btn-info btn-sm" onclick="reset('+row.id+')">重启</button>';
    return htm;
}

//查询按钮事件
$('#search_btn').click(() =>refreshData());

/**
 * 重启任务
 * @param id
 */
function reset(id) {
    getData("/scheduleJob/reset",{
        jobId: id
    })
}

/**
 * 停止任务
 * @param id
 */
function stop(id) {
    getData("/scheduleJob/stop",{
        jobId: id
    })
}

/**
 * 开始任务
 * @param id
 */
function start(id) {
    getData("/scheduleJob/start",{
        jobId: id
    })
}


function getData(url,params){
    axios.get(url, {params: params})
        .then((data) => refreshData())
        .catch((error) => console.log(error));
}


function postData(url,params) {
    axios.post(url, params)
        .then((data) => { })
        .catch((error) => console.log(error));
}

//定时刷新页面
setInterval(() => refreshData(), 60000);

//刷新数据
function refreshData() {
    $('#tab').bootstrapTable('refresh', {
        url : baseUrl+'/scheduleJob/findList',
        query:{
            title : $('#title').val(),
            className : $('#className').val()
        }
    });
}
