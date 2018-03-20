package com.kinstalk.satellite.utils;


import com.kinstalk.satellite.domain.Menu;

import java.util.List;

public class MenuUtils {

	public static String loopMenu(List<Menu> menuList) {
		StringBuffer buffer = new StringBuffer();
		/**Collections.sort(menuList);在SQL中查询顺序已经正确*/
		for (Menu menu : menuList) {
			try {
				int level = menu.getLevel().intValue();
				buffer.append("<tr onmouseover=\"this.className='dataGrid_bgcolor'\"");
				buffer.append("onmouseout=\"this.className='dataGrid_bgcolor1'\"><td>");

				int temp = level;
				while (temp-- > 0) {
					buffer.append("├─");
				}
				buffer.append(menu.getName() + "&nbsp;</td>");
				buffer.append("<td>");
				buffer.append("	<a href=\"/menu/edit?id=" + menu.getId() + "\">修改</a>");
				if (menu.getParentId() != 0) {
					buffer.append("│<a href=\"/menu/moveUp?id=" + menu.getId() + "\">上移</a>");
					buffer.append("│<a href=\"/menu/moveDown?id=" + menu.getId() + "\">下移</a>");
				} else {
					//如果根节点
					buffer.append("│<a style='color: red;' href=\"/admin/menu/moveUp?id=" + menu.getId() + "\">上移</a>");
					buffer.append("│<a style='color: red;' href=\"/admin/menu/moveDown?id=" + menu.getId() + "\">下移</a>");
				}
				buffer.append("</td>");
				buffer.append("</tr>");
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (menu.getChildren() != null) {
				buffer.append(loopMenu(menu.getChildren()));
			}
		}
		return buffer.toString();
	}
}
