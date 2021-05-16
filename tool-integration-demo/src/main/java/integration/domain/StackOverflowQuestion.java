package integration.domain;

import java.util.Date;

public class StackOverflowQuestion
{
    String tags;
    String link;
    String title;
    Date lastActivity;
    Date creationDate;
    Integer answerCount;

    public Date getCreationDate()
    {
        return this.creationDate;
    }

    public void setCreationDate(Date creationDate)
    {
        this.creationDate = creationDate;
    }

    public String getTags()
    {
        return this.tags;
    }

    public void setTags(String tags)
    {
        this.tags = tags;
    }

    public String getLink()
    {
        return this.link;
    }

    public void setLink(String link)
    {
        this.link = link;
    }

    public String getTitle()
    {
        return this.title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public Date getLastActivity()
    {
        return this.lastActivity;
    }

    public void setLastActivity(Date lastActivity)
    {
        this.lastActivity = lastActivity;
    }

    public Integer getAnswerCount()
    {
        return this.answerCount;
    }

    public void setAnswerCount(Integer answerCount)
    {
        this.answerCount = answerCount;
    }

    @Override
    public String toString()
    {
        return "SOQuestion{" +
                "tags=" + tags +
                ", link='" + link + '\'' +
                ", title='" + title + '\'' +
                ", lastActivity=" + lastActivity +
                ", creationDate=" + creationDate +
                ", answerCount=" + answerCount +
                '}';
    }
}
