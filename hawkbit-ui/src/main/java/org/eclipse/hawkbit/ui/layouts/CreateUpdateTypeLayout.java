/**
 * Copyright (c) 2015 Bosch Software Innovations GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.hawkbit.ui.layouts;

import org.eclipse.hawkbit.repository.model.DistributionSetType;
import org.eclipse.hawkbit.repository.model.NamedEntity;
import org.eclipse.hawkbit.repository.model.SoftwareModuleType;
import org.eclipse.hawkbit.ui.colorpicker.ColorPickerConstants;
import org.eclipse.hawkbit.ui.colorpicker.ColorPickerHelper;
import org.eclipse.hawkbit.ui.components.SPUIComponentProvider;
import org.eclipse.hawkbit.ui.utils.HawkbitCommonUtil;
import org.eclipse.hawkbit.ui.utils.SPUIComponentIdProvider;
import org.eclipse.hawkbit.ui.utils.SPUIDefinitions;
import org.eclipse.hawkbit.ui.utils.SPUILabelDefinitions;

import com.google.common.base.Strings;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.colorpicker.Color;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.components.colorpicker.ColorChangeEvent;
import com.vaadin.ui.components.colorpicker.ColorSelector;
import com.vaadin.ui.themes.ValoTheme;

/**
 * 
 * Superclass defining common properties and methods for creating/updating
 * types.
 *
 */
public class CreateUpdateTypeLayout extends AbstractCreateUpdateTagLayout {

    private static final long serialVersionUID = 5732904956185988397L;

    protected String createTypeStr;
    protected String updateTypeStr;
    protected TextField typeKey;

    public static final String TYPE_NAME_DYNAMIC_STYLE = "new-tag-name";
    private static final String TYPE_DESC_DYNAMIC_STYLE = "new-tag-desc";

    @Override
    protected void addListeners() {
        super.addListeners();
        optiongroup.addValueChangeListener(this::createOptionValueChanged);
    }

    @Override
    protected void createRequiredComponents() {

        createTypeStr = i18n.get("label.create.type");
        updateTypeStr = i18n.get("label.update.type");
        comboLabel = SPUIComponentProvider.getLabel(i18n.get("label.choose.type"), null);
        madatoryLabel = getMandatoryLabel();
        colorLabel = SPUIComponentProvider.getLabel(i18n.get("label.choose.type.color"), null);
        colorLabel.addStyleName(SPUIDefinitions.COLOR_LABEL_STYLE);

        tagNameComboBox = SPUIComponentProvider.getComboBox(i18n.get("label.combobox.type"), "", "", null, null, false,
                "", i18n.get("label.combobox.type"));
        tagNameComboBox.setId(SPUIDefinitions.NEW_DISTRIBUTION_SET_TYPE_NAME_COMBO);
        tagNameComboBox.addStyleName(SPUIDefinitions.FILTER_TYPE_COMBO_STYLE);
        tagNameComboBox.setImmediate(true);
        tagNameComboBox.setPageLength(SPUIDefinitions.DIST_TYPE_SIZE);

        tagColorPreviewBtn = new Button();
        tagColorPreviewBtn.setId(SPUIComponentIdProvider.TAG_COLOR_PREVIEW_ID);
        getPreviewButtonColor(ColorPickerConstants.DEFAULT_COLOR);
        tagColorPreviewBtn.setStyleName(TAG_DYNAMIC_STYLE);

        createOptionGroup(permChecker.hasCreateDistributionPermission(), permChecker.hasUpdateDistributionPermission());
    }

    @Override
    protected void setColorToComponents(final Color newColor) {

        super.setColorToComponents(newColor);
        createDynamicStyleForComponents(tagName, typeKey, tagDesc, newColor.getCSS());
    }

    /**
     * Set tag name and desc field border color based on chosen color.
     *
     * @param tagName
     * @param tagDesc
     * @param taregtTagColor
     */
    protected void createDynamicStyleForComponents(final TextField tagName, final TextField typeKey,
            final TextArea typeDesc, final String typeTagColor) {

        tagName.removeStyleName(SPUIDefinitions.TYPE_NAME);
        typeKey.removeStyleName(SPUIDefinitions.TYPE_KEY);
        typeDesc.removeStyleName(SPUIDefinitions.TYPE_DESC);
        getDynamicStyles(typeTagColor);
        tagName.addStyleName(TYPE_NAME_DYNAMIC_STYLE);
        typeKey.addStyleName(TYPE_NAME_DYNAMIC_STYLE);
        typeDesc.addStyleName(TYPE_DESC_DYNAMIC_STYLE);
    }

    /**
     * Get target style - Dynamically as per the color picked, cannot be done
     * from the static css.
     * 
     * @param colorPickedPreview
     */
    private void getDynamicStyles(final String colorPickedPreview) {

        Page.getCurrent().getJavaScript()
                .execute(HawkbitCommonUtil.changeToNewSelectedPreviewColor(colorPickedPreview));
    }

    /**
     * reset the components.
     */
    @Override
    protected void reset() {

        super.reset();
        typeKey.clear();
        restoreComponentStyles();
        setOptionGroupDefaultValue(permChecker.hasCreateDistributionPermission(),
                permChecker.hasUpdateDistributionPermission());
    }

    /**
     * Listener for option group - Create tag/Update.
     *
     * @param event
     *            ValueChangeEvent
     */
    protected void createOptionValueChanged(final ValueChangeEvent event) {

        if (updateTypeStr.equals(event.getProperty().getValue())) {
            tagName.clear();
            tagDesc.clear();
            typeKey.clear();
            typeKey.setEnabled(false);
            tagName.setEnabled(false);
            populateTagNameCombo();
            comboLayout.addComponent(comboLabel);
            comboLayout.addComponent(tagNameComboBox);
        } else {
            typeKey.setEnabled(true);
            tagName.setEnabled(true);
            window.setSaveButtonEnabled(true);
            tagName.clear();
            tagDesc.clear();
            typeKey.clear();
            comboLayout.removeComponent(comboLabel);
            comboLayout.removeComponent(tagNameComboBox);
        }
        restoreComponentStyles();
        getPreviewButtonColor(ColorPickerConstants.DEFAULT_COLOR);
        getColorPickerLayout().getSelPreview()
                .setColor(ColorPickerHelper.rgbToColorConverter(ColorPickerConstants.DEFAULT_COLOR));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.vaadin.ui.components.colorpicker.ColorSelector#setColor(com.vaadin.
     * shared.ui.colorpicker .Color)
     */
    @Override
    public void setColor(final Color color) {

        if (color == null) {
            return;
        }
        getColorPickerLayout().setSelectedColor(color);
        getColorPickerLayout().getSelPreview().setColor(getColorPickerLayout().getSelectedColor());
        final String colorPickedPreview = getColorPickerLayout().getSelPreview().getColor().getCSS();
        if (tagName.isEnabled() && null != getColorPickerLayout().getColorSelect()) {
            createDynamicStyleForComponents(tagName, typeKey, tagDesc, colorPickedPreview);
            getColorPickerLayout().getColorSelect().setColor(getColorPickerLayout().getSelPreview().getColor());
        }
    }

    /**
     * reset the tag name and tag description component border color.
     */
    @Override
    protected void restoreComponentStyles() {
        super.restoreComponentStyles();
        typeKey.removeStyleName(TYPE_NAME_DYNAMIC_STYLE);
        typeKey.addStyleName(SPUIDefinitions.TYPE_KEY);
    }

    /**
     * create option group with Create tag/Update tag based on permissions.
     */
    @Override
    protected void createOptionGroup(final boolean hasCreatePermission, final boolean hasUpdatePermission) {

        optiongroup = new OptionGroup("Select Action");
        optiongroup.addStyleName(ValoTheme.OPTIONGROUP_SMALL);
        optiongroup.addStyleName("custom-option-group");
        optiongroup.setNullSelectionAllowed(false);

        if (hasCreatePermission) {
            optiongroup.addItem(createTypeStr);
        }
        if (hasUpdatePermission) {
            optiongroup.addItem(updateTypeStr);
        }
        setOptionGroupDefaultValue(hasCreatePermission, hasUpdatePermission);
    }

    @Override
    protected void setOptionGroupDefaultValue(final boolean hasCreatePermission, final boolean hasUpdatePermission) {

        if (hasCreatePermission) {
            optiongroup.select(createTypeStr);
        }
        if (hasUpdatePermission && !hasCreatePermission) {
            optiongroup.select(updateTypeStr);
        }
    }

    protected void setColorPickerComponentsColor(final String color) {

        if (null == color) {
            getColorPickerLayout()
                    .setSelectedColor(ColorPickerHelper.rgbToColorConverter(ColorPickerConstants.DEFAULT_COLOR));
            getColorPickerLayout().getSelPreview().setColor(getColorPickerLayout().getSelectedColor());
            getColorPickerLayout().getColorSelect().setColor(getColorPickerLayout().getSelectedColor());
            createDynamicStyleForComponents(tagName, typeKey, tagDesc, ColorPickerConstants.DEFAULT_COLOR);
            getPreviewButtonColor(ColorPickerConstants.DEFAULT_COLOR);
        } else {
            getColorPickerLayout().setSelectedColor(ColorPickerHelper.rgbToColorConverter(color));
            getColorPickerLayout().getSelPreview().setColor(getColorPickerLayout().getSelectedColor());
            getColorPickerLayout().getColorSelect().setColor(getColorPickerLayout().getSelectedColor());
            createDynamicStyleForComponents(tagName, typeKey, tagDesc, color);
            getPreviewButtonColor(color);
        }
    }

    @Override
    public void colorChanged(final ColorChangeEvent event) {
        setColor(event.getColor());
        for (final ColorSelector select : getColorPickerLayout().getSelectors()) {
            if (!event.getSource().equals(select) && select.equals(this)
                    && !select.getColor().equals(getColorPickerLayout().getSelectedColor())) {
                select.setColor(getColorPickerLayout().getSelectedColor());
            }
        }
        ColorPickerHelper.setRgbSliderValues(getColorPickerLayout());
        getPreviewButtonColor(event.getColor().getCSS());
        createDynamicStyleForComponents(tagName, typeKey, tagDesc, event.getColor().getCSS());
    }

    protected Boolean checkIsDuplicate(final NamedEntity existingType) {

        if (existingType != null) {
            uiNotification.displayValidationError(
                    i18n.get("message.tag.duplicate.check", new Object[] { existingType.getName() }));
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    protected Boolean checkIsDuplicateByKey(final NamedEntity existingType) {

        if (existingType != null) {
            if (existingType instanceof DistributionSetType) {
                uiNotification.displayValidationError(i18n.get("message.type.key.duplicate.check",
                        new Object[] { ((DistributionSetType) existingType).getKey() }));
                return Boolean.TRUE;
            } else if (existingType instanceof SoftwareModuleType) {
                uiNotification.displayValidationError(i18n.get("message.type.key.swmodule.duplicate.check",
                        new Object[] { ((SoftwareModuleType) existingType).getKey() }));
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    @Override
    protected Boolean mandatoryValuesPresent() {
        if (Strings.isNullOrEmpty(tagName.getValue()) || Strings.isNullOrEmpty(typeKey.getValue())) {
            if (optiongroup.getValue().equals(createTypeStr)) {
                displayValidationError(SPUILabelDefinitions.MISSING_TYPE_NAME_KEY);
            }
            if (optiongroup.getValue().equals(updateTypeStr)) {
                if (null == tagNameComboBox.getValue()) {
                    displayValidationError(i18n.get("message.error.missing.tagName"));
                } else {
                    displayValidationError(SPUILabelDefinitions.MISSING_TAG_NAME);
                }
            }
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    @Override
    protected void save(final ClickEvent event) {
        // is implemented in the inherited class
    }

    @Override
    protected void populateTagNameCombo() {
        // is implemented in the inherited class
    }

    @Override
    protected void setTagDetails(final String tagSelected) {
        // is implemented in the inherited class
    }

}