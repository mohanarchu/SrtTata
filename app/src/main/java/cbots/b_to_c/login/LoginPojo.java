package cbots.b_to_c.login;

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
        private String[] teams;

        private String userRoleId;

        private String name;

        private String token;

        private String mobile;

        public String[] getTeams() {
            return teams;
        }

        public String getName ()
        {  return name;
        }

        public String getMobile() {
            return mobile;
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

        public String getUserRoleId() {
            return userRoleId;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [name = "+name+", token = "+token+"]";
        }

    }
}
