/*
 * ContentModuleParser.java
 *
 * Created on January 11, 2005, 1:23 PM
 *
 *  Copyright (C) 2005  Robert Cooper, Temple of the Screaming Penguin
 *
 * This library is provided under dual licenses.
 * You may choose the terms of the Lesser General Public License or the Apache
 * License at your discretion.
 *
 *  Copyright (C) 2005  Robert Cooper, Temple of the Screaming Penguin
 *
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 */
package org.rometools.feed.module.content.io;

import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.output.XMLOutputter;
import org.rometools.feed.module.content.ContentItem;
import org.rometools.feed.module.content.ContentModule;
import org.rometools.feed.module.content.ContentModuleImpl;

import java.util.ArrayList;
import java.util.List;
/**
 * @version $Revision: 1.3 $
 * @author  <a href="mailto:cooper@screaming-penguin.com">Robert "kebernet" Cooper</a>
 */
public class ContentModuleParser implements com.sun.syndication.io.ModuleParser {
    private static final Namespace CONTENT_NS = Namespace.getNamespace("content", ContentModule.URI);
    private static final Namespace RDF_NS = Namespace.getNamespace("rdf", ContentModule.RDF_URI);

    /** Creates a new instance of ContentModuleParser */
    public ContentModuleParser() {
    }

    public String getNamespaceUri() {
        return ContentModule.URI;
    }

    public com.sun.syndication.feed.module.Module parse(org.jdom2.Element element) {
        boolean foundSomething = false;
        ContentModule cm = new ContentModuleImpl();
        List encodeds = element.getChildren("encoded", CONTENT_NS);
        ArrayList contentStrings = new ArrayList();
        ArrayList encodedStrings = new ArrayList();

        if (encodeds.size() > 0) {
            foundSomething = true;

            for (Object encoded : encodeds) {
                Element encodedElement = (Element) encoded;
                encodedStrings.add(encodedElement.getText());
                contentStrings.add(encodedElement.getText());
            }
        }

        ArrayList contentItems = new ArrayList();
        List items = element.getChildren("items", CONTENT_NS);

        for (Object item1 : items) {
            foundSomething = true;

            List lis = ((Element) item1).getChild("Bag", RDF_NS).getChildren("li", RDF_NS);

            for (Object li1 : lis) {
                ContentItem ci = new ContentItem();
                Element li = (Element) li1;
                Element item = li.getChild("item", CONTENT_NS);
                Element format = item.getChild("format", CONTENT_NS);
                Element encoding = item.getChild("encoding", CONTENT_NS);
                Element value = item.getChild("value", RDF_NS);

                if (value != null) {
                    if (value.getAttributeValue("parseType", RDF_NS) != null) {
                        ci.setContentValueParseType(value.getAttributeValue("parseType", RDF_NS));
                    }

                    if ((ci.getContentValueParseType() != null) && ci.getContentValueParseType().equals("Literal")) {
                        ci.setContentValue(getXmlInnerText(value));
                        contentStrings.add(getXmlInnerText(value));
                        ci.setContentValueNamespaces(value.getAdditionalNamespaces());
                    } else {
                        ci.setContentValue(value.getText());
                        contentStrings.add(value.getText());
                    }

                    ci.setContentValueDOM(((Element) value.clone()).getContent());
                }

                if (format != null) {
                    ci.setContentFormat(format.getAttribute("resource", RDF_NS).getValue());
                }

                if (encoding != null) {
                    ci.setContentEncoding(encoding.getAttribute("resource", RDF_NS).getValue());
                }

                if (item != null) {
                    Attribute about = item.getAttribute("about", RDF_NS);

                    if (about != null) {
                        ci.setContentAbout(about.getValue());
                    }
                }

                contentItems.add(ci);
            }
        }

        cm.setEncodeds(encodedStrings);
        cm.setContentItems(contentItems);
        cm.setContents(contentStrings);

        return (foundSomething) ? cm : null;
    }

    protected String getXmlInnerText(Element e) {
        StringBuilder sb = new StringBuilder();
        XMLOutputter xo = new XMLOutputter();
        List children = e.getContent();
        sb.append(xo.outputString(children));

        return sb.toString();
    }
}
