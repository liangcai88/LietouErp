package com.ccai.utils.pca;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.ccai.utils.Tools;
import com.ccai.utils.sort.ListSortUtil;
import com.ccai.utils.words.PinYin;

public class PCAUtils {
	
	private static PCAUtils pcaUtils=null;

	private Map<Long, PCA> pcaMAP = null;

	private List<PCA> cityList = null;
	
	/**
	 * 热门城市id
	 */
	private static final int[] hotCityIds=new int[]{101,901,1901,1903,1701,1101,201,1001,2301,2201,601,1005,2701,1601,602,1502,1801,1501,1302,801,1917};
	
	private static final Map<String,String> duoyinziMap=new HashMap<String,String>();
	
	static{
		duoyinziMap.put("zhongqingshi", "chongqingshi");//重庆
	}
	
	
	public PCAUtils(){
		try {
			load();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	
	public static PCAUtils getInstance(){
		if(pcaUtils==null){
			synchronized (PCAUtils.class) {
				if(pcaUtils==null){
					pcaUtils=new PCAUtils();
				}
			}
		}
		return pcaUtils;
	}
	
	
	/**
	 * 获取热门城市列表
	 * @return
	 */
	public  List<PCA> getHotCityList(){
		List<PCA> resList=new ArrayList<PCA>();
		for (int i = 0; i < hotCityIds.length; i++) {
			resList.add(find((long)hotCityIds[i]));
		}
		return resList;
	}
	
	
	/**
	 * 根据id获取
	 * @param pcaId
	 * @return
	 */
	public  PCA findById(Long pcaId){
		return find(pcaId);
	}
	
	public  PCA findCityByCityName(String cityName){
		if(Tools.stringIsNotNull(cityName)){
			List<PCA> cityList=findCityList();
			for (int i = 0; i <cityList.size(); i++) {
				String name=cityList.get(i).getName();
				if(name.startsWith(cityName)){
					return cityList.get(i); 
				}
			}
		} 
		return new PCA(101L, 1, "北京市", 1L); 
	}
	
	
	/**
	 * 按城市名称拼音首字母返回
	 * @return
	 */
	public  List<FirstLetterCityList> getPinyinCityList(){
		List<PCA> cityList=findCityList(); 
		Map<String,List<PCA>> letterMap=new HashMap<String,List<PCA>>();
		for (int i = 0; i < cityList.size(); i++) {
			PCA pca=cityList.get(i);
			String pinyin=PinYin.pinYin(pca.getName());
			if(duoyinziMap.containsKey(pinyin)){
				pinyin=duoyinziMap.get(pinyin);
			}
			pca.setPinyin(pinyin);
			String letter=pinyin.substring(0, 1).toUpperCase();
			List<PCA> letterCityList=letterMap.get(letter);
			if(letterCityList==null){
				letterCityList=new ArrayList<PCA>();
			}
			letterCityList.add(pca);
			letterMap.put(letter, letterCityList);
		}
		
		List<FirstLetterCityList> data=new LinkedList<FirstLetterCityList>();
			Iterator<String> keyset=letterMap.keySet().iterator();
			while(keyset.hasNext()){
				String key=keyset.next();
				List<PCA> list=letterMap.get(key);
				FirstLetterCityList pl=new FirstLetterCityList();
				pl.setLetter(key);
				pl.setList(list);
				data.add(pl); 
			} 
			ListSortUtil<FirstLetterCityList> listSortUtil=new ListSortUtil<FirstLetterCityList>();
			listSortUtil.sort(data, "letter", "asc");
		 return data;
	}
	
	/**
	 * 根据id获取
	 * @param id
	 * @return
	 */
	public PCA find(Long id){
		return pcaMAP.get(id); 
	}
	
	
	/**
	 * 获取下级地区列表
	 * @param parentId
	 * @return
	 */
	public List<PCA> findChilds(Long parentId){
		List<PCA> pcaList=new ArrayList<PCA>();
		if(parentId==null || parentId.longValue()==0){
			return findPrivoceList();
		}else{
			if(parentId!=null && parentId.toString().length()<6){
				for (int i = 1; i <100; i++) {
					String id=parentId+(i<10?"0"+i:i+"");
					PCA pca=pcaMAP.get(Tools.toLong(id)); 
					if(pca!=null) 
					pcaList.add(pca);
				}
			}
		}
		return pcaList;
		
	}
	
	/**
	 * 获取省份列表
	 * @return
	 */
	public List<PCA> findPrivoceList(){
		List<PCA> list=new ArrayList<PCA>();
		for (int i = 1; i < 100; i++) {
			PCA pca=pcaMAP.get((long)i);
			if(pca!=null)  
			list.add(pca);
		}
		return list;
	}
	
	
	/**
	 * 获取城市列表
	 * @return
	 */
	public List<PCA> findCityList(){
		return cityList;
	}

	

	@SuppressWarnings("unchecked")
	public void load() throws FileNotFoundException, DocumentException {
		FileInputStream fli = new FileInputStream(Tools.getResourceFileName("config/PCA.xml"));
		SAXReader saxReader = new SAXReader();
		Document xmldoc = saxReader.read(fli);
		Element root = xmldoc.getRootElement();
		cityList = new ArrayList<PCA>();
		pcaMAP = new HashMap<Long, PCA>();
		List<Element> items = root.elements();
		if (items != null && items.size() > 0) {
			for (int i = 0; i < items.size(); i++) {// 省份列表
				System.out.println(i);
				Element proviceElement = items.get(i);
				Attribute pattributeId = proviceElement.attribute("id");
				Attribute pattributeName = proviceElement.attribute("name");
				Attribute pattributeLevel = proviceElement.attribute("level");
				PCA provice = new PCA(Tools.toLong(pattributeId.getValue()), Tools.toInt(pattributeLevel.getValue()),
						pattributeName.getValue(), 0L);
				pcaMAP.put(Tools.toLong(pattributeId.getValue()), provice);
				List<Element> cityItems = proviceElement.elements();
				if (cityItems != null && cityItems.size() > 0) {// 城市列表
					for (int j = 0; j < cityItems.size(); j++) {
						Element cityElement = cityItems.get(j);
						Attribute cattributeId = cityElement.attribute("id");
						Attribute cattributeName = cityElement.attribute("name");
						Attribute cattributeLevel = cityElement.attribute("level");
						// Attribute cattributePName
						// =cityElement.attribute("pname");
						PCA city = new PCA(Tools.toLong(cattributeId.getValue()),
								Tools.toInt(cattributeLevel.getValue()), cattributeName.getValue(), provice.getId());
						// city.setPname(cattributePName.getValue());
						provice.getChilds().add(city);
						cityList.add(city);
						pcaMAP.put(Tools.toLong(cattributeId.getValue()), city);
						List<Element> areaItems = cityElement.elements();
						if (areaItems != null && areaItems.size() > 0) {// 地区列表
							for (int k = 1; k < areaItems.size(); k++) {
								Element areaElement = areaItems.get(k);
								Attribute aattributeId = areaElement.attribute("id");
								Attribute aattributeLevel = areaElement.attribute("level");
								String name = areaElement.getTextTrim();
								PCA area = new PCA(Tools.toLong(aattributeId.getValue()),
										Tools.toInt(aattributeLevel.getValue()), name, city.getId());
								city.getChilds().add(area);
								pcaMAP.put(Tools.toLong(aattributeId.getValue()), area);
							}
						}
					}
				}
			}
		}

	}
}
