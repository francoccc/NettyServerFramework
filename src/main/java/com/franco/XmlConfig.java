package com.franco;

import com.franco.exception.ServletConfigException;
import com.franco.spring.servlet.Servlet;
import com.franco.spring.servlet.ServletConfig;
import com.google.common.base.Strings;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * XmlConfig
 * 服务器相关配置
 *
 * @author franco
 */
public class XmlConfig {

    /** 服务器servlet容器名字 */
    private String servletName;
    /** 所使用的servlet类 */
    private Class<? extends Servlet> servletClass;
    /** 初始化servlet初始属性 */
    private Map<String, Object> servletInitMap = new HashMap<String, Object>();
    /** 初始化netty初始属性 */
    private Map<String, Object> nettyInitMap = new HashMap<String, Object>();
    /** 服务器启动的所需执行的监听器 */
    private List<Class<?>> listeners;

    public XmlConfig(String path) {
        parse(path);
    }

    public ServletConfig getServletConfig() {
        return new ServletConfig() {
            public String getServletName() {
                return servletName;
            }

            public Class<? extends Servlet> getServletClass() {
                return servletClass;
            }

            public List<Class<?>> getListeners() {
                return listeners;
            }

            public Object getInitParameter(String key) {
                return servletInitMap.get(key);
            }

            public Map<String, Object> getInitParameters() {
                return servletInitMap;
            }

            public long getSessionTimeOutMillis() {
                int t = 60000 * 5;
                try {
                    t = (Integer) getInitParameter("sessionTimeOutMills");
                } catch (Exception e) {

                }
                return t;
            }

            public long getSessionInvalidateMillis() {
                return 0;
            }

            public long getSessionEmptyTimeOutMillis() {
                int t = 60000;
                try {
                    t = (Integer) getInitParameter("sessionEmptyTimeOutMills");
                } catch (Exception e) {

                }
                return t;
            }

            public long getSessionTickTime() {
                int t = 5;
                try {
                    t = (Integer) getInitParameter("sessionTickTime");
                } catch (Exception e) {

                }
                return t;
            }
        };
    }

    public NettyConfig getNettyConfig() {
        return new NettyConfig() {

            public Object getInitParameter(String key) {
                return nettyInitMap.get(key);
            }

            public Map<String, Object> getInitParameters() {
                return nettyInitMap;
            }
        };
    }

    private void parse(String path) {
        if(Strings.isNullOrEmpty(path)) {
            throw new ServletConfigException("path can not be null");
        } else {
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                factory.setValidating(false);
                factory.setNamespaceAware(false);
                DocumentBuilder builder = factory.newDocumentBuilder();
                builder.setErrorHandler(new ErrorHandler() {
                    public void warning(SAXParseException exception) throws SAXException {
                        throw exception;
                    }

                    public void error(SAXParseException exception) throws SAXException {
                        throw exception;
                    }

                    public void fatalError(SAXParseException exception) throws SAXException {
                        throw exception;
                    }
                });
                Document doc = builder.parse(XmlConfig.class.getClassLoader().getResourceAsStream(path));
                Element servletNode = (Element) doc.getElementsByTagName("servlet").item(0);
                parseNodeInitMap(servletNode, "init-param", "props", "property", servletInitMap);
                Element servletNameElement = (Element) doc.getElementsByTagName("servlet-name").item(0);
                if(null == servletNameElement) {
                    throw new ServletConfigException("servlet name not config");
                }
                servletName = servletNameElement.getTextContent();
                Element servletClassElement = (Element) doc.getElementsByTagName("servlet-class").item(0);
                if(null == servletClassElement) {
                    throw new ServletConfigException("servlet class not config");
                }
                servletClass = (Class<? extends Servlet>) Class.forName(servletClassElement.getTextContent());
                Element nettyNode = (Element) doc.getElementsByTagName("netty").item(0);
                parseNodeInitMap(nettyNode, "init-param", "props", "property", nettyInitMap);
            } catch (ParserConfigurationException e) {
                throw new ServletConfigException("parse servlet config error!");
            } catch (SAXException e) {
                throw new ServletConfigException("parse target config error!");
            } catch (IOException e) {
                throw new ServletConfigException("config IoException!");
            } catch (ClassNotFoundException e) {
                throw new ServletConfigException("not found servlet class");
            }
        }
    }

    public void parseNodeInitMap(Element root, String rootName, String secondName, String nodeName, Map<String, Object> map) {
        root = (Element) root.getElementsByTagName(rootName).item(0);
        if(null != root) {
            NodeList nodeList = root.getChildNodes();
            for(int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if(nodeList.item(i) instanceof Element) {
                    Element element = (Element) node;
                    if(element.getNodeName().equals(secondName)) {
                        NodeList _nodeList = element.getChildNodes();
                        for(int j = 0; j < _nodeList.getLength(); j++) {
                            Node _n = _nodeList.item(j);
                            if(! (_n instanceof Element)) {
                                continue;
                            }
                            Element _e = (Element) _n;
                            if(_e.getNodeName().equals(nodeName)) {
                                String name = _e.getAttribute("name");
                                String type = _e.getAttribute("type");
                                String value = _e.getTextContent();
                                map.put(name, parseValue(type, value));
                            }
                        }
                    }
                }
            }
        }
    }

    private Object parseValue(String type, String value) {
        if ("int".equalsIgnoreCase(type)) {
            return Integer.valueOf(value);
        }
        else if("boolean".equalsIgnoreCase(type)) {
            return Boolean.valueOf(value);
        }
        else if("string".equalsIgnoreCase(type)) {
            return value;
        }
        return null;
    }
}
