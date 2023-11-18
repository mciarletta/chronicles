
export default function BlankBoard({top, left, boardHeight, handleBlankBoardClick, row, col}){

    return (
        <><div className="blankBoard"
        onClick={() => handleBlankBoardClick(row, col)}
        style={{
          top: top,
          left: left,
          height: boardHeight,
          width: boardHeight,

        }}>

        </div>
        </>
    );
}