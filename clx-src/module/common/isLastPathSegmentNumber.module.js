/************************************************
 * isLastPathSegmentNumber.module.js
 * Created at 2025. 4. 28. 오후 2:47:28.
 *
 * @author gyrud
 ************************************************/

const isLastPathSegmentNumber=()=> {
	const pathName = window.location.pathname;
    const lastSegment = pathName.split("/").pop();
    const numberValue = Number(lastSegment);
    const isNumber = !isNaN(numberValue);
    return [isNumber, numberValue];
};

exports.isLastPathSegmentNumber = isLastPathSegmentNumber;