$(document).ready(function() {
	window.createGrowl = function(persistent, text, styleCss, time) {
		var target = $('.qtip.jgrowl:visible:last');
		$(document.body).qtip({
			content : {
				text : text
			},
			position : {
				my : 'top middle',
				at : (target.length ? 'bottom' : 'top') + ' middle',
				target : target.length ? target : $(document.body),
				adjust : {
					y : $(document).scrollTop()+70
				}
			},
			show : {
				event : false,
				ready : true,
				effect : function() {
					$(this).stop(0, 1).fadeIn(400);
				},
				delay : 0,
				persistent : persistent
			},
			hide : {
				event : "mousedown",
				effect : function(api) {
					$(this).stop(0, 1).fadeOut(400).queue(function() {
						api.destroy();
						updateGrowls();
					});
				}
			},
			style : {
				
				classes : styleCss,
				tip : false
			},
			events : {
				render : function(event, api) {
					timer.call(api.elements.tooltip, event, time);
				}
			}
		}).removeData('qtip');
	};
	window.updateGrowls = function() {
		var each = $('.qtip.jgrowl:not(:animated)');
		each.each(function(i) {
			var api = $(this).data('qtip');
			api.options.position.target = !i ? $(document.body) : each.eq(i - 1);
			api.set('position.at', (!i ? 'top' : 'bottom') + ' middle');
		});
	};
	function timer(event, time) {
		var api = $(this).data('qtip'), lifespan = time;
		if (api.get('show.persistent') === true) {
			return;
		}
		clearTimeout(api.timer);
		if (event.type !== 'mouseover') {
			api.timer = setTimeout(api.hide, lifespan);
		}
	}
	$(document).delegate('.qtip.jgrowl', 'mouseover mouseout', timer);
});
// 信息提示
function qtipInfo(text) {
	createGrowl(false, text, 'ui-tooltip-tips-info', 4000);
}
// 成功提示
function qtipSuccess(text) {
	createGrowl(false, text, 'tiny_tip_wrapper ui-tooltip-tips-success', 4000);
}
// 错误提示
function qtipError(text) {
	createGrowl(false, text, 'tiny_tip_wrapper ui-tooltip-tips-error', 6000);
}
// 警告提示
function qtipWarning(text) {
	createGrowl(false, text, 'tiny_tip_wrapper ui-tooltip-tips-warning', 6000);
}
