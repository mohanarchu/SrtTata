package com.example.srttata.login;

public class LoginPojo {
    private String message;

    private Results results;

    private String status;

    public String getMessage ()
    {
        return message;
    }

    public void setMessage (String message)
    {
        this.message = message;
    }

    public Results getResults ()
    {
        return results;
    }

    public void setResults (Results results)
    {
        this.results = results;
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
        return "ClassPojo [message = "+message+", results = "+results+", status = "+status+"]";
    }
    public class Results
    {

        private String name;

        private String token;

        public String getName ()
        {
            return name;
        }

        public void setName (String name)
        {
            this.name = name;
        }

        public String getToken ()
        {
            return token;
        }

        public void setToken (String token)
        {
            this.token = token;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [name = "+name+", token = "+token+"]";
        }

    }
}
