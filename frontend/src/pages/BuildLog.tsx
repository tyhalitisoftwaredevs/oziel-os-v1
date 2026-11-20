import { useState } from 'react'
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { Plus, X } from 'lucide-react'
import { api, type BuildLog } from '../api/api'

const categoryOrder:BuildLog['category'][]=['CODE','LEARNING','CAREER','LIFE']

export default function BuildLogPage(){
  const qc=useQueryClient()
  const [category,setCategory]=useState('ALL')
  const [open,setOpen]=useState(false)
  const [error,setError]=useState('')
  const logs=useQuery<BuildLog[]>({queryKey:['logs'],queryFn:async()=> (await api.get('/logs')).data})
  const stats=useQuery({queryKey:['stats'],queryFn:async()=> (await api.get('/stats/dashboard')).data})
  const create=useMutation({
    mutationFn:(body:Partial<BuildLog>)=>api.post('/logs',body),
    onSuccess:()=>{qc.invalidateQueries({queryKey:['logs']});qc.invalidateQueries({queryKey:['stats']});setOpen(false);setError('')},
    onError:()=>setError('Could not publish the log. Sign in through Admin first, then try again.')
  })
  const availableCategories=categoryOrder.filter(value=>logs.data?.some(log=>log.category===value))
  const filteredLogs=(logs.data||[]).filter(log=>category==='ALL'||log.category===category)
  function submit(e:React.FormEvent<HTMLFormElement>){
    e.preventDefault()
    const form=new FormData(e.currentTarget)
    create.mutate({title:String(form.get('title')),content:String(form.get('content')),category:String(form.get('category')) as BuildLog['category'],tags:String(form.get('tags'))})
  }
  return <div className="page">
    <header className="page-header"><div><p className="eyebrow">BUILD IN PUBLIC</p><h1>Build Log</h1></div><button className="primary" onClick={()=>{setError('');setOpen(true)}}><Plus size={17}/> New Log</button></header>
    <div className="streak-banner"><div><span>{stats.data?.streakDays??0}</span><strong>day streak</strong></div><p>Consistency turns engineering work into evidence.</p></div>
    <div className="filter-bar">{['ALL',...availableCategories].map(value=><button key={value} onClick={()=>setCategory(value)} className={category===value?'active':''}>{value}</button>)}</div>
    {logs.isLoading?<p className="muted">Loading build logs…</p>:logs.isError?<div className="panel log-empty"><h2>Build logs are unavailable</h2><p>Please check the API connection and try again.</p><button className="secondary" onClick={()=>void logs.refetch()}>Retry</button></div>:filteredLogs.length===0?
      <div className="panel log-empty"><h2>No {category==='ALL'?'build':category.toLowerCase()} entries yet</h2><p>When you record a log in this category, it will appear here.</p><button className="primary" onClick={()=>setOpen(true)}><Plus size={15}/> Write the first log</button></div>:
      <div className="timeline">{filteredLogs.map(log=><article key={log.id}><time>{new Date(log.createdAt).toLocaleDateString('en-ZA',{day:'2-digit',month:'short',year:'numeric'})}</time><div className="timeline-dot"/><div className="timeline-card"><span className="category">{log.category}</span><h3>{log.title}</h3><p>{log.content}</p><div className="tags">{log.tags?.split(',').filter(Boolean).map(tag=><span key={tag}>{tag.trim()}</span>)}</div></div></article>)}</div>}
    {open&&<div className="modal-backdrop"><form className="modal" onSubmit={submit}><button type="button" className="icon-btn" onClick={()=>setOpen(false)}><X size={18}/></button><p className="eyebrow">NEW TRANSMISSION</p><h2>Record your build</h2><label>Title<input name="title" required/></label><label>Category<select name="category"><option>CODE</option><option>LEARNING</option><option>CAREER</option><option>LIFE</option></select></label><label>Content<textarea name="content" required/></label><label>Tags<input name="tags" placeholder="Java, Spring Boot"/></label>{error&&<p className="error">{error}</p>}<button className="primary" disabled={create.isPending}>{create.isPending?'Publishing...':'Publish Log'}</button></form></div>}
  </div>
}
