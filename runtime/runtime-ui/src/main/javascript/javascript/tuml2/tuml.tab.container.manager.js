(function ($) {
    // register namespace
    $.extend(true, window, {
        Tuml: {
            TumlTabContainerManager: TumlTabContainerManager
        }
    });

    function TumlTabContainerManager(tabContainer, propertyNavigatingTo) {

        this.TumlTabContainerManager = "1.0.0";

        //This will be null for the root object only
        this.propertyNavigatingTo = propertyNavigatingTo;
        this.tabContainerProperty = null;
        this.validationResults = null;
        this.tumlTabViewManagers = [];
        this.tabContainer = null;
        this.parentTabContainer = tabContainer;
        this.parentTabContainerManager = null;

        this.destroyTabContainer = function () {
            this.clearAllTabs();
            if (this.tabContainer !== null) {
                this.tabContainer.remove();
            }
            $('#' + this.getTabId() + 'panelPanelDefault').remove();
        }

        this.showInlineForm = function() {
            this.tabLayoutTabFooterDiv.find('.btn-toolbar').hide();
            this.tabLayoutTabFooterDiv.find('.form-inline').show();
        }

        this.hideInlineForm = function() {
            this.tabLayoutTabFooterDiv.find('.form-inline').hide();
            this.tabLayoutTabFooterDiv.find('.btn-toolbar').show();
        }

        this.maybeCreateTabContainer = function () {
            //div that holds the jquery-ui tabs
            //if a tab has no open components this will be empty
            var tabContainer = $('#' + this.getTabId() + 'Tabs');
            if (tabContainer.length == 0) {

                var tabsLayout = $('#tabs-layout');

                /*
                 <div class="panel panel-default">
                 <div class="panel-heading">
                 <h3 class="panel-title"></h3>
                 </div>
                 <div class="panel-body">
                 </div>
                 <div class="panel-footer">Panel footer</div>
                 </div>
                 */

                //div that contains the panel header and body div
                //this div is defined in umlgui2.html
                //it is a bootstrap panel

                //attach the new tab to the current tabs parent. i.e. make it display below the current panel, not in it
                var tabLayoutPanelDiv = $('<div />', {id: this.getTabId() + 'panelPanelDefault', class: 'umlg-panel panel panel-default'}).appendTo(tabsLayout);

                //div that contains the tab's heading. i.e. the info, validation and warnings
                //it is a bootstrap panel header
                var tabLayoutTabHeaderDiv = $('<div />', {class: 'umlg-panel-heading panel-heading'}).appendTo(tabLayoutPanelDiv);
                var tabLayoutTabHeaderH3InfoDiv = $('<h3 />', {class: 'panel-title'}).appendTo(tabLayoutTabHeaderDiv);

                //div that contains the tabs
                //it is a bootstrap panel body
                var windowHeight = calculateBodyHeight(this);
                var tabLayoutTabBodyDiv = $('<div />', {class: 'umlg-panel-body panel-body', style: 'height:' + windowHeight + 'px'}).appendTo(tabLayoutPanelDiv);

                //div that contains the buttons
                //it is a bootstrap panel footer
                this.tabLayoutTabFooterDiv = $('<div />', {class: 'umlg-panel-footer panel-footer', role: 'navigation'}).appendTo(tabLayoutPanelDiv);

                //disable the parents save button
                if (!(this instanceof Tuml.TumlMainViewManager)) {
                    this.parentTabContainerManager.disableButtons();
                }

                this.tumlTabViewManagers = [];

                //add in the div where the property info and validation warning goes
                $('<span />', {id: this.getTabId() + 'navigation-qualified-name', class: 'navigation-qualified-name'}).appendTo(tabLayoutTabHeaderH3InfoDiv);
                $('<span />', {id: this.getTabId() + 'multiplicity-warning', class: 'multiplicity-warning pull-right text-danger'}).appendTo(tabLayoutTabHeaderH3InfoDiv);
                $('<span />', {id: this.getTabId() + 'validation-warning', class: 'validation-warning pull-right text-danger'}).appendTo(tabLayoutTabHeaderH3InfoDiv);

                this.tabContainer = $('<div />', {id: this.getTabId() + 'Tabs', class: 'umlg-tabs'}).appendTo(tabLayoutTabBodyDiv);
                this.tabContainer.append('<ul class="nav nav-tabs" />');
                this.tabContainer.append('<div class="tab-content" />');
            } else {
                throw "When does this happen!";
            }
        }

        this.createPropertyDescriptionHeading = function () {
            var multiplicity;
            if (this.tabContainerProperty.upper == -1) {
                multiplicity = 'multiplicity: [' + this.tabContainerProperty.lower + '..*]';
            } else {
                multiplicity = 'multiplicity: [' + this.tabContainerProperty.lower + '..' + this.tabContainerProperty.upper + ']';
            }
            var unique = 'unique: ' + this.tabContainerProperty.unique;
            var ordered = 'ordered: ' + this.tabContainerProperty.ordered;
            //TODO
//            var derived = 'derived: ' + propertyNavigatingTo.derived;
            var association = 'association: ' + (this.tabContainerProperty.composite ? 'composite' : 'non composite');
            return multiplicity + ', ' + unique + ', ' + ordered + ', ' + association;
        }

    }

    TumlTabContainerManager.prototype.nestedCount = function (count) {
        return this.parentTabContainerManager.nestedCount(++count);
    }

    TumlTabContainerManager.prototype.disableButtons = function () {
        var tabsNav = this.tabContainer.find('.ui-tabs-nav');
        tabsNav.find('#' + this.getTabId() + 'save').attr('disabled', 'disabled');
    }

    TumlTabContainerManager.prototype.enableButtons = function () {
        var tabsNav = this.tabContainer.find('.ui-tabs-nav');
        tabsNav.find('#' + this.getTabId() + 'save').removeAttr('disabled');
    }

    TumlTabContainerManager.prototype.removeSelectButton = function () {
        var tabsNav = this.parentTabContainer.find('.ui-tabs-nav');
        //Remove all existing open many buttons
        var buttons = tabsNav.find('.ui-button');
        for (var i = 0; i < buttons.length; i++) {
            var button = buttons[i];
            if (button.id.indexOf('OpenMany', this.length - 'OpenMany'.length) !== -1) {
                //button.remove() this does not work in firefox
                $('#' + button.id).remove();
            }
        }
    }

    TumlTabContainerManager.prototype.addSelectButton = function () {
        var self = this;
        var tabsNav = this.parentTabContainer.find('.ui-tabs-nav');

        //Remove all existing open many buttons
        this.removeSelectButton();

        var tabContainerButton = tabsNav.find('#tabcontainer-button');
        var chooseOpenMany = $('<button />', {id: this.getTabId() + 'OpenMany'}).text('Open ' + this.tabTitleName).prependTo(tabContainerButton);
        chooseOpenMany.button().click(
            function (event) {
                if (Slick.GlobalEditorLock.commitCurrentEdit()) {
                    self.openNonCompositeMany(self.propertyNavigatingTo, self.metaForData.to.qualifiedName);
                }
                event.preventDefault();
            }
        );
    }

    TumlTabContainerManager.prototype.addButtons = function () {
        var self = this;
        var buttonDiv = $('<div />', {class: "btn-toolbar"}).appendTo(this.tabLayoutTabFooterDiv);
        var buttonPullRightDiv = $('<div />', {class: "pull-right"}).appendTo(buttonDiv);
        var saveButton = $('<button />', {type: "button", id: this.getTabId() + 'save', class: 'btn btn-primary umlg-button'}).appendTo(buttonPullRightDiv);
        $('<span class="glyphicon glyphicon-save"></span>').appendTo(saveButton);
        $('<span />').text(' Save').appendTo(saveButton);
        saveButton.click(
            function (event) {
                if (Slick.GlobalEditorLock.commitCurrentEdit()) {
                    self.saveTabs();
                }
                event.preventDefault();
            }
        );
        var cancelButton = $('<button />', {type: "button", id: this.getTabId() + 'cancel', class: 'btn btn-primary umlg-button'}).appendTo(buttonPullRightDiv);
        $('<span class="glyphicon glyphicon-ban-circle"></span>').appendTo(cancelButton);
        $('<span />').text(' Cancel').appendTo(cancelButton);
        cancelButton.click(function (event) {
            self.doCancel();
            event.preventDefault();
        });

    }

    TumlTabContainerManager.prototype.updateDataModelForOneToOne = function (fakeId, fieldName, one) {
        this.parentTabContainerManager.updateDataModelForOneToOne(fakeId, fieldName, one);
    }

    TumlTabContainerManager.prototype.updateDataModelForOneToOneForUpdatedItem = function (qualifiedName, id, displayName, fieldName, one) {
        this.parentTabContainerManager.updateDataModelForOneToOneForUpdatedItem(qualifiedName, id, displayName, fieldName, one);
    }

    TumlTabContainerManager.prototype.doInternalCancel = function () {
        //Save the child's backedup data into the component's cell
        if (this.tumlTabViewManagers.length > 0) {
            if (this.tabContainerProperty == undefined) {
                //Check that it is for a lookup
                for (var i = 0; i < this.tumlTabViewManagers.length; i++) {
                    var tumlTabViewManager = this.tumlTabViewManagers[i];
                    if (tumlTabViewManager instanceof Tuml.TumlTabManyViewManager && !tumlTabViewManager.oneManyOrQuery.forLookup) {
                        throw 'A non lookup must have the tabContainerProperty set!';
                    }
                }
            } else {
                if (this.tabContainerProperty.upper == -1 || this.tabContainerProperty.upper > 1) {
                    var data = [];
                    for (var i = 0; i < this.tumlTabViewManagers.length; i++) {
                        var tumlTabViewManager = this.tumlTabViewManagers[i];
                        if (tumlTabViewManager instanceof Tuml.TumlTabManyViewManager && tumlTabViewManager.tumlTabGridManager.dataView.getItems().length > 0) {
                            data.push.apply(data, tumlTabViewManager.backupData);
                        }
                    }
                    this.setCellValue(data);
                } else {
                    if (this.tabContainerProperty.upper !== 1) {
                        alert("upper suppose to be a one");
                    }
                    var firstTumlTabViewManager = null;
                    for (var i = 0; i < this.tumlTabViewManagers.length; i++) {
                        var tumlTabViewManager = this.tumlTabViewManagers[i];
                        if (tumlTabViewManager instanceof Tuml.TumlTabOneViewManager) {
                            if (firstTumlTabViewManager !== null) {
                                alert("tabs gone wrong!");
                            }
                            firstTumlTabViewManager = tumlTabViewManager;
                        }
                    }
                    this.setCellValue(tumlTabViewManager.backupData);
                }
            }
        }
        this.destroyTabContainer();
        if (this instanceof Tuml.TumlTabManyViewManager) {

            var parentPanelPanelDefault = $("#" + this.parentTabContainerManager.getTabId() + "panelPanelDefault");
            var parentTabContent = parentPanelPanelDefault.find('.tab-content');
            parentTabContent.show();
            var height = calculateBodyHeight(this.parentTabContainerManager);
            var parentTabFooter = parentPanelPanelDefault.find('.umlg-panel-footer.panel-footer');
            parentTabFooter.show();
            parentPanelPanelDefault.children('.umlg-panel-body.panel-body').height(height - 10);

            this.tumlTabGridManager.active = true;
        }
        //enable the save button
        this.parentTabContainerManager.enableButtons();
    }

    //This only gets called by component or association class sub tabs
    TumlTabContainerManager.prototype.saveTabs = function () {
        //Save the child grids into the component's cell
        if (this.tumlTabViewManagers.length > 0) {
            if (this.tabContainerProperty == undefined || this.tabContainerProperty == null) {
                //This code executes for non composite and non primitive many properties. When adding the selected instances to the association
                //i.e. for look ups
                if (this.tumlTabViewManagers.length > 1) {
                    throw 'TumlTabContainerManager.prototype.saveTabs for lookup can only have one tab!';
                }
                var tumlTabViewManager = this.tumlTabViewManagers[0];
                var selectedRows = tumlTabViewManager.tumlTabGridManager.grid.getSelectedRows();
                var selectedItems = [];
                for (var i = 0; i < selectedRows.length; i++) {
                    var row = selectedRows[i];
                    var item = tumlTabViewManager.tumlTabGridManager.dataView.getItem(row);
                    if (tumlTabViewManager.propertyNavigatingTo.memberEndOfAssociationClass) {

                        //Need to eagerly fetch the association class's meta data.
                        //By the time the validate code runs it must already be there as the validate code must execute synchronously
                        var associationClassProperty;
                        for (var l = 0; l < this.metaForData.to.properties.length; l++) {
                            associationClassProperty = this.metaForData.to.properties[l];
                            if (associationClassProperty.name === tumlTabViewManager.propertyNavigatingTo.inverseAssociationClassPropertyName) {
                                break;
                            }
                            associationClassProperty = null;
                        }
                        Tuml.Metadata.Cache.get(associationClassProperty.qualifiedName, associationClassProperty, function () {
                        });

                        var fakeId = 'fake::' + Tuml.TumlFakeIndex++;
                        item[tumlTabViewManager.propertyNavigatingTo.inverseAssociationClassPropertyName] = {id: fakeId, tmpId: fakeId, displayName: null, refreshFromDb: true};
                    }
                    selectedItems.push(item);
                }
                this.tumlTabGridManager.addItems(selectedItems);
            } else if (!this.tabContainerProperty.associationClassOne && (this.tabContainerProperty.upper == -1 || this.tabContainerProperty.upper > 1)) {
                var data = [];
                for (var i = 0; i < this.tumlTabViewManagers.length; i++) {
                    var tumlTabViewManager = this.tumlTabViewManagers[i];
                    if (tumlTabViewManager instanceof Tuml.TumlTabManyViewManager && tumlTabViewManager.tumlTabGridManager.dataView.getItems().length > 0) {

                        var validationResult = tumlTabViewManager.validateInsert();
                        if (validationResult.length > 0) {
                            this.setValidationResults(tumlTabViewManager.metaForData.qualifiedName, validationResult);
                        }

                        data.push.apply(data, tumlTabViewManager.getTabData());
                    }
                }
                var validationResults = [];
                var multiplicity = data.length;
                if (multiplicity < this.tabContainerProperty.lower || (this.tabContainerProperty.upper !== -1 && multiplicity > this.tabContainerProperty.upper)) {
                    var validationMultiplicityResult = new Tuml.ValidationResult(this instanceof Tuml.TumlTabManyViewManager ? this.componentCell.row : "-1", this.tabContainerProperty);
                    validationMultiplicityResult.property = this.tabContainerProperty;
                    validationMultiplicityResult.message = 'multiplicity falls outside the valid range [' + this.tabContainerProperty.lower + '..' + (this.tabContainerProperty.upper !== -1 ? this.tabContainerProperty.upper : '*') + ']';
                    validationResults.push(validationMultiplicityResult);
                    this.setValidationResults(this.tabContainerProperty.qualifiedName, validationResults);
                }
                this.setCellValue(data);
            } else {
                if (!this.tabContainerProperty.associationClassOne && this.tabContainerProperty.upper !== 1) {
                    alert("upper suppose to be a one");
                }
                var tumlTabViewManager;
                for (var i = 0; i < this.tumlTabViewManagers.length; i++) {
                    tumlTabViewManager = this.tumlTabViewManagers[i];
                    if (tumlTabViewManager instanceof Tuml.TumlTabOneViewManager && tumlTabViewManager.open) {
                        break;
                    }
                }

                tumlTabViewManager.commitCurrentEdit();
                var validationResult = tumlTabViewManager.validateInsert();
                if (validationResult.length > 0) {
                    this.setValidationResults(tumlTabViewManager.metaForData.qualifiedName, validationResult);
                }

                var data = tumlTabViewManager.getTabData();
                this.setCellValue(data);
                tumlTabViewManager.tumlTabOneManager.updateDataModelForOneToOne(data);

            }

            //Clear the property. It gets set when the component or association class is opened.
            this.tabContainerProperty = null;

        }
        this.destroyTabContainer();
        //enable the save button
        this.parentTabContainerManager.enableButtons();
        this.parentTabContainerManager.updateValidationWarningHeader();
    }

    TumlTabContainerManager.prototype.internalSetComponentIdToTmpId = function (object, property) {
        var self = this;
        Tuml.Metadata.Cache.get(property.qualifiedName, property,
            function (result) {
                for (var k = 0; k < result.length; k++) {
                    self.setComponentIdToTmpId(object, result[k].meta.to);
                }
            }
        );
    }

    TumlTabContainerManager.prototype.setComponentIdToTmpId = function (item, metaDataTo) {
        //Need to update the id's to the tmpId as the id no longer exist on a rolled back transaction
        //Go through all the properties, for each composite property and associationClassOne set the id = tmpId
        if (item.tmpId !== undefined) {
            item.id = item.tmpId;
        }
        for (var i = 0; i < metaDataTo.properties.length; i++) {
            var property = metaDataTo.properties[i];
            if (!property.onePrimitive && !property.manyPrimitive) {
                var object = item[property.name];
                if (object !== undefined && object !== null) {
                    if (Array.isArray(object)) {
                        for (var j = 0; j < object.length; j++) {
                            this.internalSetComponentIdToTmpId(object[j], property);
                        }
                    } else if (!property.primitive && typeof object === 'object') {
                        this.internalSetComponentIdToTmpId(object, property);
                    }
                }
            }
        }
    }

    TumlTabContainerManager.prototype.internalClearComponentAndAssociationClassTmpId = function (object, property) {
        var self = this;
        Tuml.Metadata.Cache.get(property.qualifiedName, property,
            function (result) {
                for (var k = 0; k < result.length; k++) {
                    self.clearComponentAndAssociationClassTmpId(object, result[k].meta.to, false);
                }
            }
        );
    }

    /**
     * @param item
     * @param metaDataTo
     * @param skip only used true for the first time the mthod is called as the root item is neither a component nor an association class
     */
    TumlTabContainerManager.prototype.clearComponentAndAssociationClassTmpId = function (item, metaDataTo, skip) {
        //Need to update the id's to the tmpId as the id no longer exist on a rolled back transaction
        if (!skip && item.tmpId !== undefined) {
            item.tmpId = undefined;
        }
        for (var i = 0; i < metaDataTo.properties.length; i++) {
            var property = metaDataTo.properties[i];
            var object = item[property.name];
            if (object !== undefined && object !== null) {
                if (Array.isArray(object)) {
                    for (var j = 0; j < object.length; j++) {
                        if (property.associationClassOne && !property.memberEndOfAssociationClass) {
                            this.internalClearComponentAndAssociationClassTmpId(object[j], property);
                        }
                    }
                } else if (typeof object === 'object') {
                    if (property.associationClassOne && !property.memberEndOfAssociationClass) {
                        this.internalClearComponentAndAssociationClassTmpId(object, property);
                    }
                }
            }
        }
    }

    TumlTabContainerManager.prototype.deactivateGrids = function () {
        for (var i = 0; i < this.tumlTabViewManagers.length; i++) {
            this.tumlTabViewManagers[i].deactivateGrids();
        }
    }

    /**
     * This executes when the user presses cntrl shift save
     */
    TumlTabContainerManager.prototype.save = function () {
        for (var i = 0; i < this.tumlTabViewManagers.length; i++) {
            var tumlTabViewManager = this.tumlTabViewManagers[i];
            tumlTabViewManager.save();
        }
    }

    /**
     * This executes when the user presses cntrl shift save
     * Find the open tab with a save button and click it
     */
    TumlTabContainerManager.prototype.saveViaKeyPress = function () {
        for (var i = 0; i < this.tumlTabViewManagers.length; i++) {
            var tumlTabViewManager = this.tumlTabViewManagers[i];
            if (tumlTabViewManager.saveViaKeyPress()) {
                return true;
            }
        }
        return false;
    }

    /**
     * This executes when the user presses esc
     * Find the open tab with a cancel button and click it
     */
    TumlTabContainerManager.prototype.cancelViaKeyPress = function () {
        for (var i = 0; i < this.tumlTabViewManagers.length; i++) {
            var tumlTabViewManager = this.tumlTabViewManagers[i];
            if (tumlTabViewManager.cancelViaKeyPress()) {
                return true;
            }
        }
        return false;
    }

    TumlTabContainerManager.prototype.activeOpenTabsGrid = function () {
        for (var i = 0; i < this.tumlTabViewManagers.length; i++) {
            var tumlTabViewManager = this.tumlTabViewManagers[i];
            tumlTabViewManager.activeOpenTabsGrid();
        }
        if (this.propertyNavigatingTo !== undefined && this.propertyNavigatingTo !== null && !this.propertyNavigatingTo.associationClassOne && !this.propertyNavigatingTo.composite &&
            (this.propertyNavigatingTo.upper === -1 || this.propertyNavigatingTo.upper > 1) && !this.oneManyOrQuery.forLookup) {

            this.addSelectButton();
        } else {
            this.removeSelectButton();
        }
    }

    TumlTabContainerManager.prototype.getTabId = function () {
        alert('this must be overriden!');
    }

    TumlTabContainerManager.prototype.removeMultiplicityWarningHeader = function () {
        $('#' + this.getTabId() + 'multiplicity-warning').empty();
    }

    TumlTabContainerManager.prototype.updateNavigationHeader = function (qualifiedName) {
        $('#' + this.getTabId() + 'navigation-qualified-name').empty();
        var propertyDescription = qualifiedName;
        if (this.tabContainerProperty !== undefined && this.tabContainerProperty !== null) {
            propertyDescription += '  -  ' + this.createPropertyDescriptionHeading();
        }
        $('#' + this.getTabId() + 'navigation-qualified-name').append(propertyDescription);
    }

    TumlTabContainerManager.prototype.updateMultiplicityWarningHeader = function () {
        var multiplicityWarning = $('#' + this.getTabId() + 'multiplicity-warning');
        multiplicityWarning.empty();
        var tumlTabManyViewManagers = this.getTumlTabManyOrOneViewManagers(false);
        var rowCount = 0;
        for (var i = 0; i < tumlTabManyViewManagers.length; i++) {
            var tumlTabViewManager = tumlTabManyViewManagers[i];
            if (tumlTabViewManager instanceof Tuml.TumlTabManyViewManager) {
                rowCount += tumlTabViewManager.getTabData().length;
            } else {
                rowCount++;
            }
        }
        if (rowCount < this.tabContainerProperty.lower || (this.tabContainerProperty.upper !== -1 && rowCount > this.tabContainerProperty.upper)) {
            multiplicityWarning.append('multiplicity falls outside the valid range [' + this.tabContainerProperty.lower + '..' + (this.tabContainerProperty.upper !== -1 ? this.tabContainerProperty.upper : '*') + ']');
        }

    }

    TumlTabContainerManager.prototype.updateValidationWarningHeader = function () {
        var validationWarning = $('#' + this.getTabId() + 'validation-warning');
        validationWarning.children().remove();
        var tumlTabManyViewManagers = this.getTumlTabManyOrOneViewManagers(false);
        var fail = false;
        for (var i = 0; i < tumlTabManyViewManagers.length; i++) {
            var tumlTabViewManager = tumlTabManyViewManagers[i];
            if (tumlTabViewManager.validationResults !== null && tumlTabViewManager.validationResults.length > 0) {
                fail = true;
                break;
            }
        }
        if (fail) {
            validationWarning.append($('<span />').text('there are validation errors'));
        }
    }

    TumlTabContainerManager.prototype.getTumlTabManyOrOneViewManagers = function (commitCurrentEdit) {
        var tumlTabManyViewManagers = [];
        for (var i = 0; i < this.tumlTabViewManagers.length; i++) {
            var tumlTabViewManager = this.tumlTabViewManagers[i];
            //Get all the many tab views
            if (tumlTabViewManager instanceof Tuml.TumlTabManyViewManager || tumlTabViewManager instanceof Tuml.TumlTabOneViewManager) {
                if (commitCurrentEdit) {
                    Slick.GlobalEditorLock.commitCurrentEdit();
                }
                tumlTabManyViewManagers.push(tumlTabViewManager);
            }
        }
        return tumlTabManyViewManagers;
    }


    TumlTabContainerManager.prototype.refreshContext = function (tumlUri) {
        this.parentTabContainerManager.refreshContext(tumlUri);
    }

    TumlTabContainerManager.prototype.saveAndClearAllTabs = function () {
        //Do this in reverse as saveAndCloseTab removes the tab from the array
        var tabLength = this.tumlTabViewManagers.length;
        while (tabLength--) {
            this.tumlTabViewManagers[tabLength].saveAndCloseTab();
        }
        this.tumlTabViewManagers = null;
    }

    TumlTabContainerManager.prototype.clearAllTabs = function () {
        //Do this in reverse as closeTab removes the tab from the array
        var tabLength = this.tumlTabViewManagers.length;
        while (tabLength-- > 0) {
            this.tumlTabViewManagers[tabLength].closeTab();
        }
        this.tumlTabViewManagers = [];
    }

    TumlTabContainerManager.prototype.addToTumlTabViewManagers = function (tumlChildTabViewManager) {
        this.tumlTabViewManagers.push(tumlChildTabViewManager);
    }

    TumlTabContainerManager.prototype.saveNewRow = function () {
        alert("TumlTabContainerManager.prototype.saveNewRow must be overriden");
    }

    TumlTabContainerManager.prototype.handleDeleteRow = function () {
    }

    TumlTabContainerManager.prototype.createTabContainer = function (tabEnum, result, tumlUri, options, propertyNavigatingTo) {
        var tumlTabViewManager;
        if (options.isOne) {
            tumlTabViewManager = new Tuml.TumlTabOneViewManager(
                tabEnum,
                this.tabContainer,
                {
                    many: !options.isOne,
                    one: options.isOne,
                    query: false,
                    forLookup: options.forLookup,
                    forManyComponent: options.forManyComponent,
                    forOneComponent: options.forOneComponent,
                    forCreation: options.forCreation
                },
                tumlUri,
                result,
                propertyNavigatingTo
            );
            tumlTabViewManager.parentTabContainerManager = this;

        } else {
            if (options.forManyComponent) {
                tumlTabViewManager = new Tuml.TumlTabManyComponentViewManager(
                    tabEnum,
                    this.tabContainer,
                    {
                        many: !options.isOne,
                        one: options.isOne,
                        query: false,
                        forLookup: options.forLookup,
                        forManyComponent: options.forManyComponent
                    },
                    tumlUri,
                    result,
                    propertyNavigatingTo

                );
                tumlTabViewManager.parentTabContainerManager = this;
            } else {
                tumlTabViewManager = new Tuml.TumlTabManyViewManager(
                    tabEnum,
                    this.tabContainer,
                    {
                        many: !options.isOne,
                        one: options.isOne,
                        query: false,
                        forLookup: options.forLookup,
                        forManyComponent: options.forManyComponent
                    },
                    tumlUri,
                    result,
                    propertyNavigatingTo
                );
                tumlTabViewManager.parentTabContainerManager = this;
            }
        }
        return tumlTabViewManager;
    }
})(jQuery);
