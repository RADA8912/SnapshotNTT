package src.main.resources.script

class MetaDataBuilder extends FeaturesBuilder{

    @Override
    def buildValue(builder, String nodeName, inputValue, attrCode, multiCode, lang, origin, int count) {
        switch (nodeName){
            case ['ClassificationClass', 'ClassificationClassContentObjectReferenceMetaData']:
                builder."${nodeName}" {
                    attributeCode(attrCode)
                    attributeUnitCode(inputValue.@UnitID ?: null)
                    attributeValueCode(inputValue.@ID ?: null)
                    multiValuePosition(multiCode)
                    languageCode(lang)
                    originQualifier(origin)
                    value(inputValue)
                }
                break;
            case 'ClassificationClassAttributeAssignment':
                builder."${nodeName}" {
                    attributeCode(attrCode)
                    languageCode(lang)
                    value(inputValue)
                }
                break
            default:
                break
        }
    }

}