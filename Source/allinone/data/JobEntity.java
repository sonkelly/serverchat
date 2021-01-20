/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.data;

/**
 *
 * @author mcbvrus
 */
public class JobEntity {
    private int jobId;
    private String name;
    
    public JobEntity(int jobId, String name)
    {
        this.jobId = jobId;
        this.name = name;
    }

    /**
     * @return the jobId
     */
    public int getJobId() {
        return jobId;
    }

    /**
     * @param jobId the jobId to set
     */
    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
}
