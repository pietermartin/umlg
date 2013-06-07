(function ($) {
    // register namespace
    $.extend(true, window, {
        Tuml: {
            TumlTabContainerManager: TumlTabContainerManager
        }
    });

    function TumlTabContainerManager(tabContainer, propertyNavigatingTo) {

        this.TumlTabContainerManager = "1.0.0";
        var self = this;

        //This will be null for the root object only
        this.propertyNavigatingTo = propertyNavigatingTo;
        this.tabContainerProperty = null;
        this.validationResults = null;
        this.tumlTabViewManagers = [];
        this.tabContainer = null;
        this.parentTabContainer = tabContainer;
        this.parentTabContainerManager = null;

        this.destroyTabContainer = function () {
            $('#' + this.getTabId() + 'ui-layout-center-heading').remove();
            this.clearAllTabs();
            if (this.tabContainer !== null) {
                this.tabContainer.remove();
            }
        }

        this.maybeCreateTabContainer = function () {
            var tabLayoutDiv = $('#' + this.getTabId());
            var tabContainer = $('#' + this.getTabId() + 'Tabs');
            if (tabContainer.length == 0) {

                //disable the parents save button
                if (!(this instanceof Tuml.TumlMainViewManager)) {
                    this.parentTabContainerManager.disableButtons();
                }

                this.tumlTabViewManagers = [];

                //add in the div where the property info validation warning goes
                var uiLayoutCenterHeading = $('<div />', {id: this.getTabId() + 'ui-layout-center-heading', class: 'ui-layout-center-heading'}).appendTo(tabLayoutDiv);
                $('<div />', {id: this.getTabId() + 'navigation-qualified-name', class: 'navigation-qualified-name'}).appendTo(uiLayoutCenterHeading);
                $('<div />', {id: this.getTabId() + 'multiplicity-warning', class: 'multiplicity-warning'}).appendTo(uiLayoutCenterHeading);
                $('<div />', {id: this.getTabId() + 'validation-warning', class: 'validation-warning'}).appendTo(uiLayoutCenterHeading);

                this.tabContainer = $('<div />', {id: this.getTabId() + 'Tabs'}).appendTo(tabLayoutDiv);
                this.tabContainer.append('<ul />');
                this.tabContainer.tabs();

                this.addButtons();

                this.tabContainer.find(".ui-tabs-nav").sortable({
                    axis: "x",
                    stop: function () {
                        self.tabContainer.tabs("refresh");
                    }
                });

                this.tabContainer.tabs({
                    activate: function (event, ui) {
                        var queryId = $.data(ui.newPanel[0], 'queryId');
                        if (queryId !== undefined && queryId !== null) {
                            self.refreshQueryMenuCss(queryId);
                        }
//                        var tabEnum = $.data(ui.newPanel[0], 'tabEnum');

                        //first deactivate all grids
                        self.deactivateGrids();

                        //always set the old tab to being closed
                        for (var i = 0; i < self.tumlTabViewManagers.length; i++) {
                            var tumlTabViewManager = self.tumlTabViewManagers[i];
                            if (ui.oldPanel['0'] !== undefined && ui.oldPanel['0'].id == tumlTabViewManager.getTabId()) {
                                if (tumlTabViewManager.tumlTabGridManager !== undefined || tumlTabViewManager.tumlTabOneManager !== undefined) {
                                    tumlTabViewManager.open = false;
                                    break;
                                }
                            }
                        }

                        var tumlTabViewManagerClickedOn = null;
                        //if a leaf node then set it to open
                        for (var i = 0; i < self.tumlTabViewManagers.length; i++) {
                            var tumlTabViewManager = self.tumlTabViewManagers[i];
                            if (ui.newPanel['0'].id == tumlTabViewManager.getTabId()) {

                                tumlTabViewManagerClickedOn = tumlTabViewManager;

                                if (tumlTabViewManager.tumlTabViewManagers.length === 0) {
                                    tumlTabViewManager.open = true;
                                }
                                break;
                            }
                        }

                        tumlTabViewManagerClickedOn.activeOpenTabsGrid();

                    }
                });

                this.tabContainer.tabs({
                    beforeActivate: function (event, ui) {
                        if (!Slick.GlobalEditorLock.commitCurrentEdit()) {
                            return false;
                        } else {
                            return true;
                        }

                    }
                });

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

    TumlTabContainerManager.prototype.disableButtons = function () {
        var tabsNav = this.tabContainer.find('.ui-tabs-nav');
        tabsNav.find('#' + this.getTabId() + 'save').attr('disabled', 'disabled');
    }

    TumlTabContainerManager.prototype.enableButtons = function () {
        var tabsNav = this.tabContainer.find('.ui-tabs-nav');
        tabsNav.find('#' + this.getTabId() + 'save').removeAttr('disabled');
    }

    TumlTabContainerManager.prototype.addSelectButton = function () {
        var self = this;
        var tabsNav = this.parentTabContainer.find('.ui-tabs-nav');
        var tabsButtonDiv = tabsNav.find('#' + this.getTabId() + 'OpenMany');
        tabsButtonDiv.remove();
        var tabContainerButton = tabsNav.find('#tabcontainer-button');
        var chooseOpenMany = $('<button />', {id: this.getTabId() + 'OpenMany'}).text('Open ' + this.tabTitleName).prependTo(tabContainerButton);
        chooseOpenMany.button().click(function (event) {
            if (Slick.GlobalEditorLock.commitCurrentEdit()) {
                self.openNonCompositeMany(self.propertyNavigatingTo, self.metaForData.to.qualifiedName);
            }
            event.preventDefault();
        });
    }

    TumlTabContainerManager.prototype.addButtons = function () {
        var self = this;
        var tabsNav = this.tabContainer.find('.ui-tabs-nav');
        var tabsButtonDiv = $('<div />', {id: 'tabcontainer-button', class: 'tabs-button'}).appendTo(tabsNav);
        var saveButton = $('<button />', {id: this.getTabId() + 'save'}).text('Save').appendTo(tabsButtonDiv);
        saveButton.button().click(function (event) {
            if (Slick.GlobalEditorLock.commitCurrentEdit()) {
                self.saveTabs();
            }
            event.preventDefault();
        });
        var cancelButton = $('<button />', {id: this.getTabId() + 'save'}).text('Cancel').appendTo(tabsButtonDiv);
        cancelButton.button().click(function (event) {
            self.doCancel();
            event.preventDefault();
        });

    }

    TumlTabContainerManager.prototype.updateDataModelForOneToOne = function (fakeId, fieldName, one) {
        this.parentTabContainerManager.updateDataModelForOneToOne(fakeId, fieldName, one);
    }

    TumlTabContainerManager.prototype.updateDataModelForOneToOneForUpdatedItem = function(qualifiedName, id, displayName, fieldName, one) {
        this.parentTabContainerManager.updateDataModelForOneToOneForUpdatedItem(qualifiedName, id, displayName, fieldName, one);
    }

    TumlTabContainerManager.prototype.doCancel = function () {
        //Save the child's backedup data into the component's cell
        if (this.tumlTabViewManagers.length > 0) {
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
        this.destroyTabContainer();
        this.tumlTabGridManager.active = true;
        $('#slickGrid' + this.tabId).show();

        //enable the save button
        this.parentTabContainerManager.enableButtons();
    }

    //This only gets called by component sub tabs
    TumlTabContainerManager.prototype.saveTabs = function () {
        //Save the child grids into the component's cell
        if (this.tumlTabViewManagers.length > 0) {
            if (this.tabContainerProperty == undefined || this.tabContainerProperty == null) {
                //forLookup
                if (this.tumlTabViewManagers.length > 1) {
                    throw 'TumlTabContainerManager.prototype.saveTabs for lookup can only have one tab!';
                }
                var tumlTabViewManager = this.tumlTabViewManagers[0];
                var selectedRows = tumlTabViewManager.tumlTabGridManager.grid.getSelectedRows();
                var selectedItems = [];
                for (var i = 0; i < selectedRows.length; i++) {
                    var row = selectedRows[i];
                    selectedItems.push(tumlTabViewManager.tumlTabGridManager.dataView.getItem(row));
                }
                this.tumlTabGridManager.addItems(selectedItems);
            } else if (this.tabContainerProperty.upper == -1 || this.tabContainerProperty.upper > 1) {
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
                    var validationMultiplicityResult = new Tuml.ValidationResult(this.componentCell.row, this.tabContainerProperty);
                    validationMultiplicityResult.row = this.componentCell.row;
                    validationMultiplicityResult.property = this.tabContainerProperty;
                    validationMultiplicityResult.message = 'multiplicity falls outside the valid range [' + this.tabContainerProperty.lower + '..' + this.tabContainerProperty.upper + ']';
                    validationResults.push(validationMultiplicityResult);
                    this.setValidationResults(this.tabContainerProperty.qualifiedName, validationResults);
                }
                this.setCellValue(data);
            } else {
                if (this.tabContainerProperty.upper !== 1) {
                    alert("upper suppose to be a one");
                }
                var tumlTabViewManager;
                for (var i = 0; i < this.tumlTabViewManagers.length; i++) {
                    tumlTabViewManager = this.tumlTabViewManagers[i];
                    if (tumlTabViewManager instanceof Tuml.TumlTabOneViewManager && tumlTabViewManager.open) {
                        break;
                    }
                }

                var validationResult = tumlTabViewManager.validateInsert();
                if (validationResult.length > 0) {
                    this.setValidationResults(tumlTabViewManager.metaForData.qualifiedName, validationResult);
                }

                var data = tumlTabViewManager.getTabData();
                this.setCellValue(data);
                tumlTabViewManager.tumlTabOneManager.updateDataModelForOneToOne(data);

            }
        }
        this.destroyTabContainer();
        //enable the save button
        this.parentTabContainerManager.enableButtons();
        this.parentTabContainerManager.updateValidationWarningHeader();
    }

    TumlTabContainerManager.prototype.setComponentIdToTmpId = function (item) {
        //Need to update the id's to the tmpId as the id no longer exist on a rolled back transaction
        //Go through all the properties, for each composite property set the id = tmpId
        if (item.tmpId !== undefined) {
            item.id = item.tmpId;
        }
        for (var p in item) {
            if (item[p] !== undefined && item[p] !== null) {
                if (Array.isArray(item[p])) {

                    for (var i = 0; i < item[p].length; i++) {
                        this.setComponentIdToTmpId(item[p][i]);
                    }

                } else if (typeof item[p] === 'object') {

                    this.setComponentIdToTmpId(item[p]);

                }
            }
        }
    }

    TumlTabContainerManager.prototype.deactivateGrids = function () {
        for (var i = 0; i < this.tumlTabViewManagers.length; i++) {
            this.tumlTabViewManagers[i].deactivateGrids();
        }
    }

    TumlTabContainerManager.prototype.activeOpenTabsGrid = function () {
        for (var i = 0; i < this.tumlTabViewManagers.length; i++) {
            var tumlTabViewManager = this.tumlTabViewManagers[i];
            tumlTabViewManager.activeOpenTabsGrid();
        }
        if (this.propertyNavigatingTo !== undefined && this.propertyNavigatingTo !== null && !this.propertyNavigatingTo.composite && (this.propertyNavigatingTo.upper === -1 || this.propertyNavigatingTo.upper > 1)  && !this.oneManyOrQuery.forLookup) {
            this.addSelectButton();
        }
    }

    TumlTabContainerManager.prototype.getTabId = function () {
        alert('this must be overriden!');
    }

    TumlTabContainerManager.prototype.removeMultiplicityWarningHeader = function () {
        $('#' + this.getTabId() + 'multiplicity-warning').children().remove();
    }

    TumlTabContainerManager.prototype.updateNavigationHeader = function (qualifiedName) {
        $('#' + this.getTabId() + 'navigation-qualified-name').children().remove();
        var propertyDescription = qualifiedName;
        if (this.tabContainerProperty !== undefined && this.tabContainerProperty !== null) {
            propertyDescription += '  -  ' + this.createPropertyDescriptionHeading();
        }
        $('#' + this.getTabId() + 'navigation-qualified-name').append($('<span />').text(propertyDescription));
    }

    TumlTabContainerManager.prototype.updateMultiplicityWarningHeader = function () {
        var multiplicityWarning = $('#' + this.getTabId() + 'multiplicity-warning');
        multiplicityWarning.children().remove();
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
            multiplicityWarning.append($('<span />').text(
                'multiplicity falls outside the valid range [' + this.tabContainerProperty.lower + '..' + this.tabContainerProperty.upper + ']'));
        }

//        if (this.validationResults !== null && this.validationResults.length > 0) {
//            $('#' + this.getTabId() + 'validation-warning').append($('<span />').text(
//                ' there are validation errors'));
//        }
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

    TumlTabContainerManager.prototype.validateMultiplicity = function () {
        var tumlTabViewManagers = this.getTumlTabManyOrOneViewManagers(false);
        var rowCount = 0;
        for (var i = 0; i < tumlTabViewManagers.length; i++) {
            var tumlTabViewManager = tumlTabViewManagers[i];
            if (tumlTabViewManager instanceof  Tuml.TumlTabManyViewManager) {
                var dataView = tumlTabViewManager.tumlTabGridManager.dataView;
                rowCount += dataView.getItems().length;
            } else {
                rowCount = 1;
                break;
            }
        }
        if (rowCount < this.tabContainerProperty.lower || (this.tabContainerProperty.upper !== -1 && rowCount > this.tabContainerProperty.upper)) {
            alert('multiplicity falls outside the valid range [' + this.tabContainerProperty.lower + '..' + this.tabContainerProperty.upper + ']');
            return false;
        } else {
            return true;
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
        while (tabLength--) {
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
