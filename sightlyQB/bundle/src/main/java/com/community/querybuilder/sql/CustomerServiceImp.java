package com.community.querybuilder.sql;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ishbaner1 on 4/4/18.
 */
public class CustomerServiceImp implements CustomerService {


    public int injestCustData(String firstName, String lastName, String address, String desc) {
        Connection c=null;
        int rowCount=0;
        try
        {
            c=ConnectionHelper.getConnection();
            ResultSet rs=null;
            Statement s=c.createStatement();
            Statement scount=c.createStatement();

            PreparedStatement pstmt=null;
            PreparedStatement ps=null;
            String query="Select * from Customer";
            pstmt=c.prepareStatement(query);
            rs=pstmt.executeQuery();
            while(rs.next())
                rowCount++;
            int pkVal=rowCount+2;
            String insert="INSERT INTO Customer(custId,custFirst,custLast,custAddress,custDesc) VALUES(?, ?, ?, ?, ?);";
            ps=c.prepareStatement(insert);
            ps.setInt(1,pkVal);
            ps.setString(2,firstName);
            ps.setString(3,lastName);
            ps.setString(4,address);
            ps.setString(5,desc);
            ps.execute();
            return pkVal;

        }

        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            ConnectionHelper.close(c);
        }
        return 0;
    }

    public String getCustomerData(String filter) {
        Connection c=null;
        int rowCount=0;
        Customer cust=null;
        String query="";
        List<Customer> custList=new ArrayList<Customer>();
        try{
            c=ConnectionHelper.getConnection();
            ResultSet rs=null;
            Statement s=c.createStatement();
            Statement scount=c.createStatement();
            PreparedStatement pstmt=null;
            PreparedStatement ps=null;
            if(filter.equals("All Customers"))
                query="Select custId,custFirst,custLast,custAddress,custDesc FROM Customer;";
            else if (filter.equals("Past Customer"))
                query = "Select custId,custFirst,custLast,custAddress,custDesc FROM Customer where custDesc = 'Past Customer'; ";
            else if (filter.equals("Active Customer"))
                query="Select custId,custFirst,custLast,custAddress,custDesc FROM Customer where custDesc = 'Active Customer'; ";
            pstmt=c.prepareStatement(query);
            rs=pstmt.executeQuery();

            while(rs.next())
            {
                cust=new Customer();
                int custId=rs.getInt(1);
                String id = Integer.toString(custId);
                cust.setCustId(id);

                cust.setCustFirst(rs.getString(2));
                cust.setCustLast(rs.getString(3));
                cust.setCustAddress(rs.getString(4));
                cust.setCustDescription(rs.getString(5));
                custList.add(cust);
            }
            return convertToString(toXml(custList));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    private Document toXml(List<Customer> custList)
    {
        try{
            DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
            DocumentBuilder builder=factory.newDocumentBuilder();
            Document doc =builder.newDocument();
            Element root=doc.createElement("Customers");
            doc.appendChild(root);
            int custCount=custList.size();
            for(int index=0; index<custCount;index++)
            {
                Customer myCust=(Customer)custList.get(index);
                Element Customer=doc.createElement("Customer");
                root.appendChild(Customer);

                Element first=doc.createElement("First");
                first.appendChild(doc.createTextNode(myCust.getCustFirst()));
                Customer.appendChild(first);

                Element last=doc.createElement("Last");
                last.appendChild(doc.createTextNode(myCust.getCustLast()));
                Customer.appendChild(last);

                Element address = doc.createElement( "Address" );
                address.appendChild( doc.createTextNode(myCust.getCustAddress()) );
                Customer.appendChild( address );


            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String convertToString(Document xml)
    {
        try
        {
            Transformer transformer=TransformerFactory.newInstance().newTransformer();
            StreamResult result=new StreamResult(new StringWriter());
            DOMSource source=new DOMSource(xml);
            transformer.transform(source,result);
            return result.getWriter().toString();

        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return null;
    }
}
