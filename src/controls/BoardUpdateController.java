package controls;

import java.util.Map;

import bind.DataBinding;
import dao.BoardDao;
import vo.Board;

public class BoardUpdateController implements Controller, DataBinding{
	BoardDao boardDao;
	
	public BoardUpdateController setBoardDao(BoardDao boardDao) {
		this.boardDao = boardDao;
		return this;
	}

	@Override
	public Object[] getDataBinders() {
		return new Object[] {
				"bno", Integer.class,
				"board" , vo.Board.class
		};
	}

	@Override
	public String execute(Map<String, Object> model) throws Exception {
		Board board = (Board) model.get("board");
		if(board.getTitle()==null) {
			Integer bno = (Integer) model.get("bno");
			Board  detailInfo = boardDao.detail(bno);
			model.put("board", detailInfo);
			return "/board/BoardUpdateForm.jsp";
		}else {
			boardDao.update(board);
			return "redirect:list.do";
		}
	}

}
