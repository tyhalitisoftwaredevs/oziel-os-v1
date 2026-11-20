import axios from 'axios'

export const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api/v1',
  timeout: 10000,
})

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('oziel-token')
  if (token) config.headers.Authorization = `Bearer ${token}`
  return config
})

export type Project = { id:number; title:string; slug:string; problem:string; solution:string; techStack:string[]; status:'LIVE'|'IN_PROGRESS'|'CONCEPT'; demoUrl?:string; githubUrl?:string; imageUrl?:string; impact:string; featured:boolean; createdAt:string }
export type BuildLog = { id:number; title:string; content:string; category:'CODE'|'LEARNING'|'CAREER'|'LIFE'; tags:string; createdAt:string; imageUrl?:string }
export type Opportunity = { id:number; name:string; company:string; type:string; closingDate:string|null; status:string; link:string }
