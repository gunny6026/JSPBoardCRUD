package controls.board;

import java.util.Map;

import annotation.Component;
import controls.Controller;
import dao.MySqlBoardDAO;

@Component("/board/list.do")
public class BoardListController implements Controller {
	MySqlBoardDAO boardDao;
	
	public BoardListController setBoardDao(MySqlBoardDAO boardDao) {
		this.boardDao = boardDao;
		return this;
	}

	@Override
	public String execute(Map<String, Object> model) throws Exception {
		int pagenum =1;
		model.put("boards", boardDao.list(pagenum));
		return "/board/BoardList.jsp";
	}

}
