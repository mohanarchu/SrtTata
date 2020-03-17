package cbots.b_to_c.CA.Models;

public class CarModels {
    private Value[] value;

    private String status;

    public Value[] getValue ()
    {
        return value;
    }

    public void setValue (Value[] value)
    {
        this.value = value;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [value = "+value+", status = "+status+"]";
    }
    public class Value
    {
        private String model;

        private String _id;

        private String category;

        private Varients[] variants;

        public String get_id ()
        {
            return _id;
        }

        public void set_id (String _id)
        {
            this._id = _id;
        }

        public String getCategory ()
        {
            return category;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public void setCategory (String category)
        {
            this.category = category;
        }

        public Varients[] getVarients ()
        {
            return variants;
        }

        public void setVarients (Varients[] varients)
        {
            this.variants = varients;
        }


    }

    public class Varients
    {
        private String color;

        private String price;

        private String name;

        public String getColor ()
        {
            return color;
        }

        public void setColor (String color)
        {
            this.color = color;
        }

        public String getPrice ()
        {
            return price;
        }

        public void setPrice (String price)
        {
            this.price = price;
        }

        public String getName() {
            return name;
        }
    }
}
