import {ErrorCode} from "./ErrorCode";

export default class NullPointerError extends Error {
    private _errorCode: ErrorCode;
    constructor(errCode: ErrorCode) {
        super();
        this._errorCode = errCode;
    }

    public get errorCode() {
        return this._errorCode;
    }
}
