package com.community.querybuilder.scheduler;


import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.felix.scr.annotations.Reference;
import org.apache.sling.commons.scheduler.Scheduler;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


@Component(metatype = true)
@Service
public class SimpleDSComponent implements Runnable {

    protected final Logger log= LoggerFactory.getLogger(this.getClass());
    private BundleContext bundleContext;
    @Reference
    private Scheduler scheduler;
    public void run() {
        log.info("Running");

    }
    protected void activate(ComponentContext ctx)
    {
        this.bundleContext=ctx.getBundleContext();
        String jobName3="case3";
        String schedulingExp="0 15 10 ? * MON-FRI";
        final Date firstDate=new Date();
        Map<String,Serializable> config3=new HashMap<String, Serializable>();
        final Runnable job=new Runnable() {
            public void run() {
                int staleItems=checkStaleItems();
                if(staleItems>6)
                {
                    sendMail(staleItems);
                }
            }
        };
        try
        {
            this.scheduler.addJob("myJob",job,null,schedulingExp,true);
        } catch (Exception e) {
            job.run();
        }
    }
    protected void deactivate(ComponentContext ctx)
    {
        this.bundleContext=null;
    }
    private int checkStaleItems()
    {
        try
        {
            MBeanServer server= ManagementFactory.getPlatformMBeanServer();
            ObjectName workflowMBean=getWorkflowMBean(server);
            Object staleWorkflowCount=server.invoke(workflowMBean,"countStaleWorkflows",new Object[]{null},new String[]{String.class.getName()});
            int mystaleCount=(Integer)staleWorkflowCount;
            return mystaleCount;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return -1;
    }

    private static ObjectName getWorkflowMBean(MBeanServerConnection server)
    {
        try
        {
            Set<ObjectName> names=server.queryNames(new ObjectName("com.adobe.granite.workflow:type=Maintenance,*"), null);
            if(names.isEmpty())
            {
                return null;
            }
            return names.iterator().next();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;

    }

    private void sendMail(int count)
    {
        String to="set the to address";
        String from="set the from address";
        String host="set SMTP mail server";
        java.util.Properties properties= System.getProperties();
        properties.setProperty("mail.smtp.host","mail.google.com");
        properties.setProperty("mail.smtp.user","ishikabanerjee9@gmail.com");
        Session session=Session.getDefaultInstance(properties);
        try {
            MimeMessage message=new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("Stale AEM Workflow Items");
            message.setText("Please note that there are "+count +" stale AEM workflows");
            Transport.send(message);
            log.info("Stale mail notification message sent message successfully....");
        }catch (Exception ex) {
            ex.printStackTrace();
        }

        }


    }
