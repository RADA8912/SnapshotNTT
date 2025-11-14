package src.main.resources.script

public class IntegrationObjectItem implements Serializable{

    private String code;
    private IntegrationType type;
    private IntegrationObject integrationObject;
    private List<IntegrationObjectItemAttribute> attributes;
    private Boolean  root;

    public IntegrationObjectItem(String code,IntegrationType type,IntegrationObject integrationObject,Boolean  root, List<IntegrationObjectItemAttribute> attributes )
    {
        this.setCode(code);
        this.setType(type);
        this.setIntegrationObject(integrationObject);
        this.setRoot(root);
        this.setAttributes(attributes);
    }

    public IntegrationObjectItem(IntegrationObjectItem clone)
    {
        this.code=clone.code;
        this.type=new IntegrationType.Builder().withCode(clone.getType().getCode()).build();
        this.integrationObject=new IntegrationObject.Builder().withCode(clone.getIntegrationObject().getCode()).build();
        this.root=clone.root;
    }

    public IntegrationObjectItem()
    {

    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public IntegrationType getType() {
        return type;
    }

    public void setType(IntegrationType type) {
        this.type = type;
    }

    public IntegrationObject getIntegrationObject() {
        return integrationObject;
    }

    public void setIntegrationObject(IntegrationObject integrationObject) {
        this.integrationObject = integrationObject;
    }

    public List<IntegrationObjectItemAttribute> getAttributes() {
        if(attributes==null){
             attributes=new ArrayList<>();
        }
        return attributes;
    }

    public void setAttributes(List<IntegrationObjectItemAttribute> attributes) {
        this.attributes = attributes;
    }

    public Boolean getRoot() {
        return root;
    }

    public void setRoot(Boolean  root) {
        this.root = root;
    }


    public static class Builder{
        private String code;
        private IntegrationType type;
        private IntegrationObject integrationObject;
        private List<IntegrationObjectItemAttribute> attributes;
        private Boolean  root;
        public Builder()
        {

        }

        public Builder withCode(String code)
        {
            this.code=code;
            return this;
        }

        public Builder withIntegrationType(IntegrationType type)
        {
            this.type=type;
            return this;
        }

        public Builder withIntegrationObject(IntegrationObject integrationObject)
        {
            this.integrationObject=integrationObject;
            return this;
        }

        public Builder withRoot(Boolean  root)
        {
            this.root=root;
            return this;
        }

        public Builder withAttributes(List<IntegrationObjectItemAttribute> attributes)
        {
            this.attributes=attributes;
            return this;
        }

        public IntegrationObjectItem build()
        {
            return new IntegrationObjectItem(code,type,integrationObject,root,attributes);
        }


    }
}