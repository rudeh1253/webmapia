import axios from "axios";

const axiosGameService = axios.create({
    baseURL: "http://localhost:8002", // TODO: Move this hard coded constant into .env file
    headers: {
        "Content-Type": "application/json"
    },
    withCredentials: true
});

export default axiosGameService;
