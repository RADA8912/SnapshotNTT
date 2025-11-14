package src.main.resources.script

public class IntegrationObjectItemAttribute implements Serializable{
    private IntegrationObjectItem integrationObjectItem;
    private String attributeName;
    private boolean unique;
    private boolean autoCreate;
    private AttributeDescriptor attributeDescriptor;
    private IntegrationObjectItem returnIntegrationObjectItem;

    public IntegrationObjectItemAttribute(IntegrationObjectItem integrationObjectItem,
                                          String attributeName,boolean unique, boolean autoCreate,AttributeDescriptor attributeDescriptor,
                                          IntegrationObjectItem returnIntegrationObjectItem )
    {
        this.setIntegrationObjectItem(integrationObjectItem);
        this.setAttributeName(attributeName);
        this.setUnique(unique);
        this.setAutoCreate(autoCreate);
        this.setAttributeDescriptor(attributeDescriptor);
        this.setReturnIntegrationObjectItem(returnIntegrationObjectItem);
    }

    public  IntegrationObjectItemAttribute()
    {

    }

    public IntegrationObjectItem getIntegrationObjectItem() {
        return integrationObjectItem;
    }

    public void setIntegrationObjectItem(IntegrationObjectItem integrationObjectItem) {
        this.integrationObjectItem = integrationObjectItem;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public boolean isUnique() {
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    public boolean isAutoCreate() {
        return autoCreate;
    }

    public void setAutoCreate(boolean autoCreate) {
        this.autoCreate = autoCreate;
    }

    public AttributeDescriptor getAttributeDescriptor() {
        return attributeDescriptor;
    }

    public void setAttributeDescriptor(AttributeDescriptor attributeDescriptor) {
        this.attributeDescriptor = attributeDescriptor;
    }

    public IntegrationObjectItem getReturnIntegrationObjectItem() {
        return returnIntegrationObjectItem;
    }

    public void setReturnIntegrationObjectItem(IntegrationObjectItem returnIntegrationObjectItem) {
        this.returnIntegrationObjectItem = returnIntegrationObjectItem;
    }

    public static class Builder
    {
        private IntegrationObjectItem integrationObjectItem;
        private String attributeName;
        private boolean unique;
        private boolean autoCreate;
        private AttributeDescriptor attributeDescriptor;
        private IntegrationObjectItem returnIntegrationObjectItem;

        public Builder withIntegrationObjectItem(IntegrationObjectItem integrationObjectItem)
        {
            this.integrationObjectItem=integrationObjectItem;
            return this;
        }

        public Builder withAttributeName(String attributeName)
        {
            this.attributeName=attributeName;
            return this;
        }

        public Builder withUnique(boolean unique)
        {
            this.unique=unique;
            return this;
        }

        public Builder withAutoCreate(boolean autoCreate)
        {
            this.autoCreate=autoCreate;
            return this;
        }

        public Builder withAttributeDescriptor(AttributeDescriptor attributeDescriptor)
        {
            this.attributeDescriptor=attributeDescriptor;
            return this;
        }

        public  Builder withReturnIntegrationObjectItem(IntegrationObjectItem returnIntegrationObjectItem)
        {
            this.returnIntegrationObjectItem=returnIntegrationObjectItem;
            return this;
        }

        public IntegrationObjectItemAttribute build()
        {
            return new IntegrationObjectItemAttribute(integrationObjectItem,attributeName,unique,autoCreate,attributeDescriptor,returnIntegrationObjectItem);
        }
    }
}