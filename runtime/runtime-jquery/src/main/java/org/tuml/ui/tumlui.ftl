<html>
<head>	
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />  
	<!-- /restAndJson must be come a template variable, resolved by restlet to the app name -->
	<link type="text/css" rel="stylesheet" href="/${app.rootUrl}/javascript/layout/layout-default-latest.css" />
	<link rel="stylesheet" href="/${app.rootUrl}/javascript/slickgrid/slick.grid.css" type="text/css"/>
	<link rel="stylesheet" href="/${app.rootUrl}/javascript/slickgrid/css/smoothness/jquery-ui-1.8.16.custom.css" type="text/css"/>

	<script src="/${app.rootUrl}/javascript/slickgrid/lib/jquery-1.7.min.js"></script>
	<script src="/${app.rootUrl}/javascript/slickgrid/lib/jquery-ui-1.8.16.custom.min.js"></script>
	<script src="/${app.rootUrl}/javascript/slickgrid/lib/jquery.event.drag-2.0.min.js"></script>
	<script src="/${app.rootUrl}/javascript/layout/jquery.layout-latest.js"></script>
	<script src="/${app.rootUrl}/javascript/slickgrid/slick.core.js"></script>
	<script src="/${app.rootUrl}/javascript/slickgrid/slick.grid.js"></script>
	<script src="/${app.rootUrl}/javascript/slickgrid/slick.formatters.js"></script>
	<script src="/${app.rootUrl}/javascript/slickgrid/tuml.slick.formatters.js"></script>
	<script>

	var myLayout;
	$(document).ready(function() {
		// $('body').layout({ applyDefaultStyles: true, resizable: true });
		// init instance var
		var myLayout = $('body').layout({

			//	reference only - these options are NOT required because 'true' is the default
			closable: true // pane can open & close
			,
			resizable: true // when open, pane can be resized 
			,
			slidable: true // when closed, pane can 'slide' open over other panes - closes on mouse-out
			,
			livePaneResizing: true

			//	some resizing/toggling settings
			,
			north__slidable: false // OVERRIDE the pane-default of 'slidable=true'
			,
			north__togglerLength_closed: '100%' // toggle-button is full-width of resizer-bar
			,
			north__spacing_closed: 20 // big resizer-bar when open (zero height)
			,
			south__resizable: false // OVERRIDE the pane-default of 'resizable=true'
			,
			south__spacing_open: 0 // no resizer-bar when open (zero height)
			,
			south__spacing_closed: 20 // big resizer-bar when open (zero height)
			//	some pane-size settings
			,
			west__minSize: 100,
			east__size: 300,
			east__minSize: 200,
			east__maxSize: .5 // 50% of layout width
			,
			center__minWidth: 100

			//	some pane animation settings
			,
			west__animatePaneSizing: false,
			west__fxSpeed_size: "fast" // 'fast' animation when resizing west-pane
			,
			west__fxSpeed_open: 1000 // 1-second animation when opening west-pane
			,
			west__fxSettings_open: {
				easing: "easeOutBounce"
			} // 'bounce' effect when opening
			,
			west__fxName_close: "none" // NO animation when closing west-pane
			//	enable showOverflow on west-pane so CSS popups will overlap north pane
			,
			west__showOverflowOnHover: true

			//	enable state management
			,
			stateManagement__enabled: true // automatic cookie load & save enabled by default
			,
			showDebugMessages: true // log and/or display messages from debugging & testing code
		});

		// layout utilities
		// myLayout.addPinBtn("#myPinButton", "west");
		myLayout.allowOverflow("north");

		refreshPageTo("${app.uri}");

	});

	window.onpopstate = function(event) {
		var pathname = document.location.pathname.replace("/ui", "");
	    refreshPageTo(pathname);
	};

	//This adds in the /ui part after /${app.rootUrl}
	function change_my_url(title, url)	{
		var urlToPush = '/${app.rootUrl}/ui' +  url.substring('${app.rootUrl}/'.length);
		history.pushState({}, title, urlToPush);
	}

	function refreshPageTo(tumlUri) {

		var jqxhr = $.getJSON(tumlUri, function(response, b, c) {
			var classNameLowerCased;
			var menuArray = [];
			var metaForData = {};
			var isOne;
			if (response.meta instanceof Array) {
				//Property is a many, meta has 2 properties, one for both sides of the association
				//The first one is the parent property, i.e. the context property for which the menu is built
				isOne = false;
				var contextMeta = response.meta[0];
				classNameLowerCased = contextMeta.name.toLowerCase();
				$.each(contextMeta.properties, function(index, metaProperty) {
					menuArray[index] = metaProperty;
				});
				metaForData = response.meta[1];
				createPageForMany(response.data, metaForData, metaForData.uri);
			} else {
				//Property is a one
				isOne = true;
				$.each(response.meta.properties, function(index, metaProperty) {
					menuArray[index] = metaProperty;
				});
				classNameLowerCased = response.meta.name.toLowerCase();
				metaForData = response.meta;
				createPageForOne(response.data, metaForData);
			}

			var vertexId = tumlUri.match(/\d+/);
			if (vertexId != null) {
				createLeftMenu(menuArray, classNameLowerCased, vertexId[0]);
			} else {
				createLeftMenu(menuArray);
			}

		}).fail(function(a, b, c) {
			alert("error " + a + ' ' + b + ' ' + c);
		});

	}

	function createPageForMany(data, metaForData, currentUri) {
		$('.ui-layout-center').children().remove();
		var gridData = [];
		$.each(data, function(index, rowArray) {
			var gridObject = {};
			for(var i=0, len=rowArray.length; i < len; i++) {
				var rowPropertyObject = rowArray[i]; 
				gridObject[rowPropertyObject.name] = rowPropertyObject.value;
			}
			//Add in url to object
			gridObject['link'] = currentUri;

			gridData[index] = gridObject;
			// console.log(data);
		});
		$('<div class="grid-header" style="width:100%"><label>' + metaForData.name + ':</label></div><div id="myGrid" style="width:100%;height:500px;"></div>').appendTo(".ui-layout-center");
		createGrid(gridData);
	}

	function createPageForOne(data, metaForData) {
		$('.ui-layout-center').children().remove();

		$('<div>' + metaForData.name + '</div>').appendTo('.ui-layout-center');

		$.each(metaForData.properties, function(index, property) {
			if (property.onePrimitive) {

				$.each(data, function(index, value) {
					if (value.name == property.name) {
						$('<div><label for="' + property.name + 'Id">' + property.name + '</label><input id="' + property.name + 'Id" type="text" class="field" name="' + property.name + '" value="' + value.value + '" /></div>').appendTo('.ui-layout-center');
					}
				});

			}
		});

	}

	function createGrid(data) {
		var grid;
		var columns = [{
			id: "id",
			name: "id",
			field: "id"
		}, {
			id: "name",
			name: "name",
			field: "name"
		}, {
			id: "link", name: "Link", field: "link", width: 80, resizable: true, formatter: TumlSlick.Formatters.Link
		}
		];
		var options = {
			enableCellNavigation: true,
			enableColumnReorder: false
		};
		grid = new Slick.Grid("#myGrid", data, columns, options);
	}

	function createLeftMenu(menuArray, classNameLowerCased, vertexId) {
		$('.ui-left-menu-link').children().remove();
		$.each(menuArray, function(index, value) {
			var adjustedUri = menuArray[index].tumlUri.replace(new RegExp("\{(\s*?.*?)*?\}", 'gi'), vertexId);
			// var adjustedUri = menuArray[index].tumlUri.replace('{' + classNameLowerCased + 'Id}', vertexId);
			$('.ui-left-menu-link').append('<li>').append(
				$('<a>',{
						text: menuArray[index].name,
						title: menuArray[index].name,
						href: adjustedUri,
						click: function(){ 
							change_my_url( menuArray[index].name, adjustedUri );
							refreshPageTo( adjustedUri );return false;
						}
					}
				)).append('</li>');
		});
	}



</script>
</head>
<body>
	<div class="ui-layout-center">
		
  </div>
	</div>
	<div class="ui-layout-north">North</div>
	<div class="ui-layout-south">South</div>
	<div class="ui-layout-east">East</div>
	<div class="ui-layout-west">
		<ul class="ui-left-menu-link">
		</ul>
	</div>
</body>
</html>