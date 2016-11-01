/*  
    http://www.dailycoding.com/ 
    Topbar message plugin
*/
(function ($) {
    $.fn.showTopbarMessage = function (options) {

        var defaults = {
            background: "#888",
            borderColor: "#000",
            foreColor: "#000",
            height: "50px",
            fontSize: "20px",
            close: "click"
        };  
        var options = $.extend(defaults, options);
                    
        var barStyle = "z-index:20000; width: 350px;position: fixed;height: " + options.height + ";top: 0px;left: 50%;right: 0px;margin: 0 0 0 -175px;display: none;";      
        var overlayStyle = "height: " + options.height + ";filter: alpha(opacity=80);-moz-opacity: 0.8;-khtml-opacity: 0.8;opacity: 0.8;background-color: " + options.background + ";border-bottom: solid 0px " + options.borderColor + ";";
        var messageStyle = " width: 100%;line-height:"+options.height+";position: absolute;height: " + options.height + ";top: 0px;left: 0px;right: 0px;margin: 0px;color: " + options.foreColor + ";font-size: " + options.fontSize + ";text-align: center;padding: 0";

        return this.each(function () {
            obj = $(this);

            if ($(".topbarBox").length > 0) {
                // Hide already existing bars
                $(".topbarBox").hide()
                $(".topbarBox").slideUp(100, function () {
                    $(".topbarBox").remove();
                });
            }
 

            var html = ""
                + "<div class='topbarBox' style='" + barStyle + "'>"
                + "  <div style='" + overlayStyle + "'>&nbsp;</div>"
                + "  <div style='" + messageStyle + "'>" + obj.html() + "</div>"
                + "</div>"

            if (options.close == "click") {
                $(html).click(function () {
                    $(this).slideUp(100, function () {
                        $(this).remove();
                    });
                }).appendTo($('body')).slideDown(100);
            } 
            else {
                $(html).appendTo($('body')).slideDown(100).delay(options.close).slideUp(100, function () {
                    $(this).remove();
                });
            }

        });
    };
})(jQuery);