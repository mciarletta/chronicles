import { useEffect, useRef, useCallback, useState } from "react";

export default function Die({ sides, size, die, returnDie, rolling }) {
  const thisDice = useRef("");

  const [dieStyle, setDieStyle] = useState({
    position: "relative",
    width: `${size}px`,
    height: `${size}px`,
    transformStyle: "preserve-3d",
    transform: `perspective(750px) rotateY(0deg) rotateX(0deg) rotateZ(0deg)`,
  });

  const updateDie = useCallback(() => {


    let newRotX;
    let newRotY;
    let newRotZ;

    if (die.winningSide){
    const winningSplit = die.winningSide.split(',')


    // Set the die style to rotate to that side
    switch (winningSplit[0]) {
      case "1":
        newRotX = 0;
        newRotY = 0;
        newRotZ = 0;
        break;
      case "2":
        newRotX = 0;
        newRotY = 90;
        newRotZ = 0;

        break;
      case "3":
        newRotX = 90;
        newRotY = 0;
        newRotZ = 0;

        break;
      case "4":
        newRotX = 270;
        newRotY = 0;
        newRotZ = 0;

        break;
      case "5":
        newRotX = 0;
        newRotY = 270;
        newRotZ = 0;

        break;
      default:
        newRotX = 0;
        newRotY = 180;
        newRotZ = 0;

        break;
    }

    // Update the dieStyle object
    setDieStyle((prevStyle) => ({
      ...prevStyle,
      transform: `perspective(750px) rotateY(${newRotY + 5}deg) rotateX(${
        newRotX + 5
      }deg) rotateZ(${newRotZ + 5}deg)`,
    }));
  }
  }, [die]);

  useEffect(() => {
    const dice = thisDice.current;

    if (die.rolling === true) {
      dice.classList.add("rollDice");
    } else {
      dice.classList.remove("rollDice");
    }

    updateDie();

  }, [die, updateDie, rolling]);

  let sidesList = [];

  for (const s of sides) {
    let sideTrans;
    let sideText;
    switch (s) {
      case 1:
        sideTrans = `translateZ(${size / 2}px)`;
        sideText = die.side1;
        break;
      case 6:
        sideTrans = `translateZ(-${size / 2}px) rotateY(180deg)`;
        sideText = die.side6;
        break;
      case 2:
        sideTrans = `translateX(-${size / 2}px) rotateY(-90deg)`;
        sideText = die.side2;
        break;
      case 5:
        sideTrans = `translateX(${size / 2}px) rotateY(90deg)`;
        sideText = die.side5;
        break;
      case 3:
        sideTrans = `translateY(${size / 2}px) rotateX(270deg)`;
        sideText = die.side3;
        break;
      case 4:
        sideTrans = `translateY(-${size / 2}px) rotateX(90deg)`;
        sideText = die.side4;
        break;
      default:
        sideTrans = `translateZ(${size / 2}px)`;
    }
    sidesList.push(
      <div
        key={s}
        style={{
          width: "100%",
          height: "100%",
          background: die.background,
          color: die.color,
          border: "2px solid black",
          position: "absolute",
          opacity: "0.9",
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
          transform: `${sideTrans}`,
        }}
      >
        {sideText}
      </div>
    );
  }

  return (
    <div style={dieStyle} onClick={() => returnDie(die)} ref={thisDice}>
      {sidesList}
    </div>
  );
}
