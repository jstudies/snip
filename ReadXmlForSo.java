package edu.javacourse.stp.db;

import edu.javacourse.stp.domain.PersonAdult;
import edu.javacourse.stp.domain.PersonChild;
import edu.javacourse.stp.domain.StudentOrder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ReadXmlForSo {

    public static List<StudentOrder> getStudentOrders() {
        try {
            List<StudentOrder> so = new ArrayList<>();
            DocumentBuilder docBuild = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document XMLdoc = docBuild.parse("student_orders.xml");
            Node root = XMLdoc.getDocumentElement();
            List<Integer> ids = extractOrdersID(root);

            for (Integer id : ids) {
                int p = extractChildrenQty(root, id);
                so.add(getStudentOrder(root, id, p));
            }
            return so;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static StudentOrder getStudentOrder(Node root, Integer id, int p) {
        PersonAdult h = null;
        PersonAdult w = null;
        List<PersonChild> children = new ArrayList<>();

        try {
            // making grown-ups
            for (int i = 0; i < 2; i++) {
                if (i == 0) {
                    h = makePerson(root, id, i);
                } else {
                    w = makePerson(root, id, i);
                }
            }
            // making kids
            for (int i = 1; i < p + 1; i++) {
                children.add(makeChild(root, id, i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        System.out.println(w.getSurName());
        StudentOrder so = new StudentOrder(h, w, children, id);
        return so;
    }

    private static List<Integer> extractOrdersID(Node root) throws XPathExpressionException {
        XPathFactory pf = XPathFactory.newInstance(); // initiate XPath
        XPath xp = pf.newXPath();
        List<Integer> result = new ArrayList<>(); // make list
        XPathExpression expr = xp.compile("student-order"); // set expression to search
        NodeList nodes = (NodeList) expr.evaluate(root, XPathConstants.NODESET); // load nodes
        for (int i = 0; i < nodes.getLength(); i++) {
            Node n = nodes.item(i); // assign node to n
            int id = Integer.parseInt(((Element) n).getAttribute("so-id")); // parse for attribute so-id
            result.add(id); // add result to the list
        }
        return result;
    }

    private static int extractChildrenQty(Node root, Integer id) throws XPathExpressionException {
        XPathFactory pf = XPathFactory.newInstance(); // initiate XPath
        XPath xp = pf.newXPath();
        String who = String.format("student-order[@so-id='%d']/children", id); // which instance to stop at
        XPathExpression expr = xp.compile(who);
        NodeList nodes = (NodeList) expr.evaluate(root, XPathConstants.NODESET);
        Node n = nodes.item(0); // assign node to n
        int qty = Integer.parseInt(((Element) n).getAttribute("ch-qty"));
        System.out.println("SO: " + id + ", children qty: " + qty);
        return qty;
    }

    private static PersonAdult makePerson(Node root, Integer id, int p) throws XPathExpressionException {

        String type;
        if (p == 0) {
            type = "husband";
        } else {
            type = "wife";
        }

        PersonAdult person = new PersonAdult();
        XPathFactory pf = XPathFactory.newInstance(); // initiate XPath
        XPath xp = pf.newXPath();
        String who = String.format("student-order[@so-id='%d']/" + type, id); // which instance to stop at
        XPathExpression expr = xp.compile(who);
        Node nodes = (Node) expr.evaluate(root, XPathConstants.NODE); // load nodes
        NodeList names = nodes.getChildNodes();

        for (int i = 0; i < names.getLength(); i++) {
            Node name = names.item(i);
            if (name instanceof Element) {
                if ("surName".equals(name.getNodeName())) {
                    person.setSurName(name.getTextContent().trim());
                }
                if ("givenName".equals(name.getNodeName())) {
                    person.setGivenName(name.getTextContent());
                }
                if ("patronymic".equals(name.getNodeName())) {
                    person.setPatronymic(name.getTextContent());
                }
                if ("surName".equals(name.getNodeName())) {
                    person.setSurName(name.getTextContent());
                }
                if ("dateOfBirth".equals(name.getNodeName())) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                    try {
                        Date dob = sdf.parse(name.getTextContent());
                        person.setDateOfBirth(dob);
                    } catch (ParseException pex) {
                        pex.printStackTrace(System.out);
                    }
                }
            }
        }
        return person;
    }

    private static PersonChild makeChild(Node root, Integer id, int p) throws XPathExpressionException {
        PersonChild person = new PersonChild();
        XPathFactory pf = XPathFactory.newInstance(); // initiate XPath
        XPath xp = pf.newXPath();
        String who = String.format("student-order[@so-id='%d']/children/child[" + p + "]", id);
        XPathExpression expr = xp.compile(who); // set expression to search
        Node nodes = (Node) expr.evaluate(root, XPathConstants.NODE); // load nodes
        NodeList names = nodes.getChildNodes();

        for (int i = 0; i < names.getLength(); i++) {
            Node name = names.item(i);
            if (name instanceof Element) {
                if ("surName".equals(name.getNodeName())) {
                    person.setSurName(name.getTextContent().trim());
                }
                if ("givenName".equals(name.getNodeName())) {
                    person.setGivenName(name.getTextContent());
                }
                if ("patronymic".equals(name.getNodeName())) {
                    person.setPatronymic(name.getTextContent());
                }
                if ("surName".equals(name.getNodeName())) {
                    person.setSurName(name.getTextContent());
                }
                if ("dateOfBirth".equals(name.getNodeName())) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                    try {
                        Date dob = sdf.parse(name.getTextContent());
                        person.setDateOfBirth(dob);
                    } catch (ParseException pex) {
                        pex.printStackTrace(System.out);
                    }
                }
            }
        }
        return person;
    }
}
