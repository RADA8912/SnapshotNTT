package src.main.resources.script

public class IntegrationObject implements Serializable{

    private String code;
    private IntegrationType integrationType;
    private List<IntegrationObjectItem> items;

    public IntegrationObject()
    {

    }

    public IntegrationObject(String code, IntegrationType integrationType,List<IntegrationObjectItem> items )
    {
        this.setCode(code);
        this.setIntegrationType(integrationType);
        this.setItems(items);
    }
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public IntegrationType getIntegrationType() {
        return integrationType;
    }

    public void setIntegrationType(IntegrationType integrationType) {
        this.integrationType = integrationType;
    }


    public List<IntegrationObjectItem> getItems() {
        if(this.items==null)
        {
            items=new ArrayList<>();
        }
        return items;
    }

    public void setItems(List<IntegrationObjectItem> items) {
        this.items = items;
    }

   public static class Builder
    {
        private String code;
        private IntegrationType integrationType;
        private List<IntegrationObjectItem> items;
        public Builder()
        {

        }
        public Builder withCode(String code)
        {
            this.code=code;
            return this;
        }

        public Builder withIntegrationType(IntegrationType integrationType)
        {
            this.integrationType=integrationType;
            return this;

        }

        public Builder withItems(List<IntegrationObjectItem> items)
        {
            this.items=items;
            return this;
        }

        public IntegrationObject build()
        {
            return new IntegrationObject(this.code, this.integrationType,this.items);
        }



    }
}