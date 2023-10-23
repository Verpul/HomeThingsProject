import axios from "axios";

export default function setHeader(token) {
  axios.defaults.headers.common['Authorization'] = 'Bearer ' + token;
}