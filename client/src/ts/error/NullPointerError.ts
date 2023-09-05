import {ErrorCode} from "./ErrorCode";

export default class NullPointerError extends Error {
    private _errorCode: ErrorCode;
    constructor(message: string, errCode: ErrorCode) {
        super(message);
        this._errorCode = errCode;
    }

    public get errorCode() {
        return this._errorCode;
    }
}
