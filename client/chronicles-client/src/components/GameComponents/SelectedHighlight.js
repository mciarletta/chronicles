
export default function SelectedHighlight({height, color="yellow", posX, posY}) {

    const selectedStyle = {
        position: 'absolute',
        top: posY,
        left: posX,
        height: height,
        width: height,
        border: `5px solid ${color}`,
        zIndex: 1,
    }



    return(
        <>

        <div style={selectedStyle}>
        </div>
        </>
    );
}