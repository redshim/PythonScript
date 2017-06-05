import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
 
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;
 
public class HtmlTableTagParse {
    public static void main(String[] args) throws MalformedURLException, IOException {
        //송장 번호를 받는다.
        //String num = "602411606750";
         
        //송장 번호와 cj택배에 배송추적 관련 주소와 연결 시킨다.
        String url = "http://www.cars.com/go/alg/index.jsp";
         
        //해당 URL 페이지를 가져온다.
        Source source = new Source(new URL(url));
         
        //메소드 찾기를 위해 시작부터 끝까지 태그들만 parse 한다 (?)
        source.fullSequentialParse();
         
        //해당 데이터가 있는 부분을 찾는 부분.
        Element div = source.getAllElements(HTMLElementName.DIV).get(1);
         
        Element table = div.getAllElements(HTMLElementName.TABLE).get(0);
         
        List trList = table.getAllElements(HTMLElementName.TR);
         
        Iterator trIter = trList.iterator();
         
        trIter.next();
         
        while(trIter.hasNext()){
            Element tr = (Element) trIter.next();
 
            List dataList = tr.getAllElements(HTMLElementName.TD);
             
            Iterator dataIter = dataList.iterator();
             
            int chk = 0;
             
            List resultList = new ArrayList();
             
            //원하는 결과 값이 들어가는 부분.
            List rowList = new ArrayList();
             
            while(dataIter.hasNext()){
                Element data = (Element) dataIter.next();
                String value = data.getContent().getTextExtractor().toString();
                rowList.add(chk,value);
                 
                chk++;
                 
                if(chk == 5){
                    resultList.add(rowList);
                    chk = 0;
                }
            }
             
            //콘솔 창에서 확인 하기 위한 부분.
            Iterator resultIter = resultList.iterator();
             
            while(resultIter.hasNext()){
                List list = (ArrayList)resultIter.next();
                 
                System.out.println((String)list.get(0) + list.get(1) + 
                                      list.get(2) + list.get(3) + list.get(4));
            }
        }
         
    }
}