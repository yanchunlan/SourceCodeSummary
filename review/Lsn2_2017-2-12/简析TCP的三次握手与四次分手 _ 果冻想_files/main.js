if( !window.console ){
    window.console = {
        log: function(){}
    }
}


/* 
 * jsui
 * ====================================================
*/
jsui.bd = $('body')
jsui.is_signin = jsui.bd.hasClass('logged-in') ? true : false;

if( $('.widget-nav').length ){
    $('.widget-nav li').each(function(e){
        $(this).hover(function(){
            $(this).addClass('active').siblings().removeClass('active')
            $('.widget-navcontent .item:eq('+e+')').addClass('active').siblings().removeClass('active')
        })
    })
}

if( $('.sns-wechat').length ){
    $('.sns-wechat').on('click', function(){
        var _this = $(this)
        if( !$('#modal-wechat').length ){
            $('body').append('\
                <div class="modal fade" id="modal-wechat" tabindex="-1" role="dialog" aria-hidden="true">\
                    <div class="modal-dialog" style="margin-top:200px;width:340px;">\
                        <div class="modal-content">\
                            <div class="modal-header">\
                                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>\
                                <h4 class="modal-title">'+ _this.attr('title') +'</h4>\
                            </div>\
                            <div class="modal-body" style="text-align:center">\
                                <img style="max-width:100%" src="'+ _this.data('src') +'">\
                            </div>\
                        </div>\
                    </div>\
                </div>\
            ')
        }
        $('#modal-wechat').modal()
    })
}


if( $('.carousel').length ){
    var el_carousel = $('.carousel')

    el_carousel.carousel({
        interval: 4000
    })

    require(['hammer'], function(Hammer) {

        // window.Hammer = Hammer
        
        var mc = new Hammer(el_carousel[0]);

        mc.on("panleft panright swipeleft swiperight", function(ev) {
            if( ev.type == 'swipeleft' || ev.type == 'panleft' ){
                el_carousel.carousel('next')
            }else if( ev.type == 'swiperight' || ev.type == 'panright' ){
                el_carousel.carousel('prev')
            }
        });

    })
}


if( Number(jsui.ajaxpager) > 0 && ($('.excerpt').length || $('.excerpt-minic').length) ){
    require(['ias'], function() {
        if( !jsui.bd.hasClass('site-minicat') && $('.excerpt').length ){
            $.ias({
                triggerPageThreshold: jsui.ajaxpager?Number(jsui.ajaxpager)+1:5,
                history: false,
                container : '.content',
                item: '.excerpt',
                pagination: '.pagination',
                next: '.next-page a',
                loader: '<div class="pagination-loading"><img src="'+jsui.uri+'/img/loading.gif"></div>',
                trigger: 'More',
                onRenderComplete: function() {
                    require(['lazyload'], function() {
                        $('.excerpt .thumb').lazyload({
                            data_attribute: 'src',
                            placeholder: jsui.uri + '/img/thumbnail.png',
                            threshold: 400
                        });
                    });
                }
            });
        }

        if( jsui.bd.hasClass('site-minicat') && $('.excerpt-minic').length ){
            $.ias({
                triggerPageThreshold: jsui.ajaxpager?Number(jsui.ajaxpager)+1:5,
                history: false,
                container : '.content',
                item: '.excerpt-minic',
                pagination: '.pagination',
                next: '.next-page a',
                loader: '<div class="pagination-loading"><img src="'+jsui.uri+'/img/loading.gif"></div>',
                trigger: 'More',
                onRenderComplete: function() {
                    require(['lazyload'], function() {
                        $('.excerpt .thumb').lazyload({
                            data_attribute: 'src',
                            placeholder: jsui.uri + '/img/thumbnail.png',
                            threshold: 400
                        });
                    });
                }
            });
        }
    });
}


/* 
 * lazyload
 * ====================================================
*/
if ( $('.thumb:first').data('src') || $('.widget_ui_posts .thumb:first').data('src') || $('.wp-smiley:first').data('src') || $('.avatar:first').data('src')) {
    require(['lazyload'], function() {
        $('.avatar').lazyload({
            data_attribute: 'src',
            placeholder: jsui.uri + '/img/avatar-default.png',
            threshold: 400
        })

        $('.widget .avatar').lazyload({
            data_attribute: 'src',
            placeholder: jsui.uri + '/img/avatar-default.png',
            threshold: 400
        })

        $('.thumb').lazyload({
            data_attribute: 'src',
            placeholder: jsui.uri + '/img/thumbnail.png',
            threshold: 400
        })

        $('.widget_ui_posts .thumb').lazyload({
            data_attribute: 'src',
            placeholder: jsui.uri + '/img/thumbnail.png',
            threshold: 400
        })

        $('.wp-smiley').lazyload({
            data_attribute: 'src',
            // placeholder: jsui.uri + '/img/thumbnail.png',
            threshold: 400
        })
    })
}


/* 
 * prettyprint
 * ====================================================
*/
$('pre').each(function(){
    if( !$(this).attr('style') ) $(this).addClass('prettyprint')
})

if( $('.prettyprint').length ){
    require(['prettyprint'], function(prettyprint) {
        prettyPrint()
    })
}



/* 
 * rollbar
 * ====================================================
*/
jsui.rb_comment = ''
if (jsui.bd.hasClass('comment-open')) {
    jsui.rb_comment = "<li><a href=\"javascript:(scrollTo('#comments',-15));\"><i class=\"fa fa-comments\"></i></a><h6>去评论<i></i></h6></li>"
}

jsui.bd.append('\
    <div class="m-mask"></div>\
    <div class="rollbar"><ul>'
    +jsui.rb_comment+
    '<li><a href="javascript:(scrollTo());"><i class="fa fa-angle-up"></i></a><h6>去顶部<i></i></h6></li>\
    </ul></div>\
')



var _wid = $(window).width()
video_ok()
$(window).resize(function(event) {
    _wid = $(window).width()
    video_ok()
});



var scroller = $('.rollbar')
var _fix = (jsui.bd.hasClass('nav_fixed') && !jsui.bd.hasClass('page-template-navs')) ? true : false
$(window).scroll(function() {
    var h = document.documentElement.scrollTop + document.body.scrollTop

    if( _fix && h > 0 && _wid > 720 ){
        jsui.bd.addClass('nav-fixed')
    }else{
        jsui.bd.removeClass('nav-fixed')
    }

    h > 200 ? scroller.fadeIn() : scroller.fadeOut();
})


/* 
 * bootstrap
 * ====================================================
*/
$('.user-welcome').tooltip({
    container: 'body',
    placement: 'bottom'
})



/* 
 * sign
 * ====================================================
*/
if (_wid>720 && !jsui.bd.hasClass('logged-in')) {
    require(['signpop'], function(signpop) {
        signpop.init()
    })
}


/* 
 * single
 * ====================================================
*/

var _sidebar = $('.sidebar')
if (_wid>1024 && _sidebar.length) {
    var h1 = 15,
        h2 = 30
    var rollFirst = _sidebar.find('.widget:eq(' + (jsui.roll[0] - 1) + ')')
    var sheight = rollFirst.height()

    rollFirst.on('affix-top.bs.affix', function() {
        rollFirst.css({
            top: 0
        })
        sheight = rollFirst.height()

        for (var i = 1; i < jsui.roll.length; i++) {
            var item = jsui.roll[i] - 1
            var current = _sidebar.find('.widget:eq(' + item + ')')
            current.removeClass('affix').css({
                top: 0
            })
        };
    })

    rollFirst.on('affix.bs.affix', function() {
        rollFirst.css({
            top: h1
        })

        for (var i = 1; i < jsui.roll.length; i++) {
            var item = jsui.roll[i] - 1
            var current = _sidebar.find('.widget:eq(' + item + ')')
            current.addClass('affix').css({
                top: sheight + h2
            })
            sheight += current.height() + 15
        };
    })

    rollFirst.affix({
        offset: {
            top: _sidebar.height(),
            bottom: $('.footer').outerHeight()
        }
    })


}







$('.plinks a').each(function(){
    var imgSrc = $(this).attr('href')+'/favicon.ico'
    $(this).prepend( '<img src="'+imgSrc+'">' )
})


/* 
 * comment
 * ====================================================
*/
if (jsui.bd.hasClass('comment-open')) {
    require(['comment'], function(comment) {
        comment.init()
    })
}


/* 
 * page u
 * ====================================================
*/
if (jsui.bd.hasClass('page-template-pagesuser-php')) {
    require(['user'], function(user) {
        user.init()
    })
}


/* 
 * page theme
 * ====================================================
*/
if (jsui.bd.hasClass('page-template-pagestheme-php')) {
    require(['theme'], function(theme) {
        theme.init()
    })
}


/* 
 * page nav
 * ====================================================
*/
if( jsui.bd.hasClass('page-template-pagesnavs-php') ){

    var titles = ''
    var i = 0
    $('#navs .items h2').each(function(){
        titles += '<li><a href="#'+i+'">'+$(this).text()+'</a></li>'
        i++
    })
    $('#navs nav ul').html( titles )

    $('#navs .items a').attr('target', '_blank')

    $('#navs nav ul').affix({
        offset: {
            top: $('#navs nav ul').offset().top,
            bottom: $('.footer').height() + $('.footer').css('padding-top').split('px')[0]*2
        }
    })


    if( location.hash ){
        var index = location.hash.split('#')[1]
        $('#navs nav li:eq('+index+')').addClass('active')
        $('#navs nav .item:eq('+index+')').addClass('active')
        scrollTo( '#navs .items .item:eq('+index+')' )
    }
    $('#navs nav a').each(function(e){
        $(this).click(function(){
            scrollTo( '#navs .items .item:eq('+$(this).parent().index()+')' )
            $(this).parent().addClass('active').siblings().removeClass('active')
        })
    })
}


/* 
 * page search
 * ====================================================
*/
if( jsui.bd.hasClass('search-results') ){
    var val = $('.site-search-form .search-input').val()
    var reg = eval('/'+val+'/i')
    $('.excerpt h2 a, .excerpt .note').each(function(){
        $(this).html( $(this).text().replace(reg, function(w){ return '<b>'+w+'</b>' }) )
    })
}


/* 
 * search
 * ====================================================
*/
$('.search-show').bind('click', function(){
    $(this).find('.fa').toggleClass('fa-remove')

    jsui.bd.toggleClass('search-on')

    if( jsui.bd.hasClass('search-on') ){
        $('.site-search').find('input').focus()
        jsui.bd.removeClass('m-nav-show')
    }
})

/* 
 * phone
 * ====================================================
*/

jsui.bd.append( $('.site-navbar').clone().attr('class', 'm-navbar') )

$('.m-icon-nav').on('click', function(){
    jsui.bd.addClass('m-nav-show')

    $('.m-mask').show()

    jsui.bd.removeClass('search-on')
    $('.search-show .fa').removeClass('fa-remove') 
})

$('.m-mask').on('click', function(){
    $(this).hide()
    jsui.bd.removeClass('m-nav-show')
})




if ($('.article-content').length){
    $('.article-content img').attr('data-tag', 'bdshare')
}

function video_ok(){
    $('.article-content embed, .article-content video, .article-content iframe').each(function(){
        var w = $(this).attr('width'),
            h = $(this).attr('height')
        if( h ){
            $(this).css('height', $(this).width()/(w/h))
        }
    })
}




/* 
 * baidushare
 * ====================================================
*/
if( $('.bdsharebuttonbox').length ){

    if ($('.article-content').length) $('.article-content img').data('tag', 'bdshare')

    window._bd_share_config = {
        common: {
            "bdText": '',
            "bdMini": "2",
            "bdMiniList": false,
            "bdPic": '',
            "bdStyle": "0",
            "bdSize": "24"
        },
        share: [{
            // "bdSize": 12,
            bdCustomStyle: jsui.uri + '/css/share.css'
        }]/*,
        slide : {    
            bdImg : 4,
            bdPos : "right",
            bdTop : 200
        },
        image: {
            tag: 'bdshare',
            "viewList": ["qzone", "tsina", "weixin", "tqq", "sqq", "renren", "douban"],
            "viewText": " ",
            "viewSize": "16"
        },
        selectShare : {
            "bdContainerClass":'article-content',
            "bdSelectMiniList":["qzone","tsina","tqq","renren","weixin"]
        }*/
    }

    with(document)0[(getElementsByTagName('head')[0]||body).appendChild(createElement('script')).src='http://bdimg.share.baidu.com/static/api/js/share.js?cdnversion='+~(-new Date()/36e5)];
}



/* functions
 * ====================================================
 */
function scrollTo(name, add, speed) {
    if (!speed) speed = 300
    if (!name) {
        $('html,body').animate({
            scrollTop: 0
        }, speed)
    } else {
        if ($(name).length > 0) {
            $('html,body').animate({
                scrollTop: $(name).offset().top + (add || 0)
            }, speed)
        }
    }
}


function is_name(str) {
    return /.{2,12}$/.test(str)
}
function is_url(str) {
    return /^((http|https)\:\/\/)([a-z0-9-]{1,}.)?[a-z0-9-]{2,}.([a-z0-9-]{1,}.)?[a-z0-9]{2,}$/.test(str)
}
function is_qq(str) {
    return /^[1-9]\d{4,13}$/.test(str)
}
function is_mail(str) {
    return /^[_a-z0-9-]+(\.[_a-z0-9-]+)*@[a-z0-9-]+(\.[a-z0-9-]+)*(\.[a-z]{2,4})$/.test(str)
}


$.fn.serializeObject = function(){
    var o = {};
    var a = this.serializeArray();
    $.each(a, function() {
        if (o[this.name] !== undefined) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};


function strToDate(str, fmt) { //author: meizz   
    if( !fmt ) fmt = 'yyyy-MM-dd hh:mm:ss'
    str = new Date(str*1000)
    var o = {
        "M+": str.getMonth() + 1, //月份   
        "d+": str.getDate(), //日   
        "h+": str.getHours(), //小时   
        "m+": str.getMinutes(), //分   
        "s+": str.getSeconds(), //秒   
        "q+": Math.floor((str.getMonth() + 3) / 3), //季度   
        "S": str.getMilliseconds() //毫秒   
    };
    if (/(y+)/.test(fmt))
        fmt = fmt.replace(RegExp.$1, (str.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt))
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}



