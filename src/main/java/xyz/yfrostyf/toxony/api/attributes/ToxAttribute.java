package xyz.yfrostyf.toxony.api.attributes;

import net.minecraft.world.entity.ai.attributes.RangedAttribute;

public class ToxAttribute extends RangedAttribute {
    /* For reference, RangeAttribute is used to replicate attributes like how minecraft does it.
    * We extend RangeAttributes to add our own custom attributes into
    * the registry using the following parameters in the constructor:
    *
    * descriptionId:    Unique String ID of attribute to separate it from the others
    * defaultValue:     Default double value when applied, cannot be lower than minValue or higher than maxValue
    * minValue:         Minimum double value the attribute will go
    * maxValue:         Maximum double value the attribute will go
    */
    public ToxAttribute(String inDescriptionId, double inDefaultValue, double inMinValue, double inMaxValue) {
        super(inDescriptionId, inDefaultValue, inMinValue, inMaxValue);
    }
}
