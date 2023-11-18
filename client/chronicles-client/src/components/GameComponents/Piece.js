export default function Piece({
  coordinates,
  boardOffsetTop,
  boardOffsetLeft,
  height,
  scale,
  skin,
  color,
  scaleSize = 1
}) {
  const yTrans = (height / 2) * scale * scaleSize; //TODO: have pieces with their own information from the db?
  const zTrans = -height / 2 + (height / 2) * scale * scaleSize;
  const heightToScale = height * scale * scaleSize;
  let leftScaleOffset;
  if (scaleSize === 1){
    leftScaleOffset = 0;
  } else  {
    leftScaleOffset = -(height *0.9 * scaleSize / 2) + height / 2;
  }

  let pieceStyle = {
    position: "absolute",
    top: coordinates.top + boardOffsetTop,
    left: coordinates.left + boardOffsetLeft + height / 9 + leftScaleOffset,
    height: heightToScale,
    width: height * 0.8 * scaleSize,
    backgroundColor: color, 
    transform: `rotate3d(1, 0, 0, 90deg) translateY(${yTrans}px) rotateZ(180deg) translateZ(${zTrans}px) `, //note: rotateY will roatate it's facing direction on the board TODO: allow users to rotate
    transition: "top 3s, left 3s",
  };

  return (
    <div style={pieceStyle}>
      <img
        src={skin}
        alt="Piece"
        style={{
          height: heightToScale * 0.90,
          width: height * 0.8 * scaleSize,
        }}
      ></img>
    </div>
  );
}
