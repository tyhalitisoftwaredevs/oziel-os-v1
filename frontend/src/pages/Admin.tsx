import { useState } from 'react'
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { ShieldCheck, Trash2, Plus, X, Pencil } from 'lucide-react'
import { api, type Project, type BuildLog, type Opportunity, type Certification } from '../api/api'

export default function Admin(){
  const [token,setToken]=useState(localStorage.getItem('oziel-token'))
  const [email,setEmail]=useState('admin@ozielos.co.za');const [password,setPassword]=useState('')
  const [error,setError]=useState('')
  const login=useMutation({mutationFn:()=>api.post('/auth/login',{email,password}),onSuccess:r=>{localStorage.setItem('oziel-token',r.data.token);setToken(r.data.token)},onError:()=>setError('Invalid admin credentials')})
  if(!token)return <div className="login-wrap"><form className="login-card panel" onSubmit={e=>{e.preventDefault();login.mutate()}}><div className="brand"><span className="brand-mark">O.</span><span>OZIEL <b>OS</b></span></div><ShieldCheck size={28}/><p className="eyebrow">AUTHORIZED SYSTEM</p><h1>Admin login</h1><label>Email<input value={email} onChange={e=>setEmail(e.target.value)} type="email"/></label><label>Password<input value={password} onChange={e=>setPassword(e.target.value)} type="password"/></label>{error&&<p className="error">{error}</p>}<button className="primary" disabled={login.isPending}>Unlock dashboard</button></form></div>
  return <AdminDashboard/>
}

function AdminDashboard(){
  const qc=useQueryClient();const projects=useQuery<Project[]>({queryKey:['admin-projects'],queryFn:async()=> (await api.get('/projects')).data})
  const logs=useQuery<BuildLog[]>({queryKey:['admin-logs'],queryFn:async()=> (await api.get('/logs')).data})
  const alerts=useQuery<any[]>({queryKey:['admin-alerts'],queryFn:async()=> (await api.get('/admin/notifications')).data})
  const certifications=useQuery<Certification[]>({queryKey:['admin-certifications'],queryFn:async()=> (await api.get('/admin/certifications')).data})
  const [showOpportunityForm,setShowOpportunityForm]=useState(false)
  const [opportunityError,setOpportunityError]=useState('')
  const [showCertificationForm,setShowCertificationForm]=useState(false)
  const [editingCertification,setEditingCertification]=useState<Certification|null>(null)
  const [certificationError,setCertificationError]=useState('')
  const addOpportunity=useMutation({
    mutationFn:(body:Partial<Opportunity>)=>api.post('/opportunities',body),
    onSuccess:()=>{setShowOpportunityForm(false);setOpportunityError('');qc.invalidateQueries({queryKey:['admin-alerts']});qc.invalidateQueries({queryKey:['opportunities']})},
    onError:()=>setOpportunityError('Could not add opportunity. Check the form and try again.')
  })
  const saveCertification=useMutation({
    mutationFn:(input:{id:number|null;body:Omit<Certification,'id'>})=>input.id===null
      ?api.post('/admin/certifications',input.body)
      :api.put(`/admin/certifications/${input.id}`,input.body),
    onSuccess:()=>{setShowCertificationForm(false);setEditingCertification(null);setCertificationError('');qc.invalidateQueries({queryKey:['admin-certifications']});qc.invalidateQueries({queryKey:['certifications']})},
    onError:()=>setCertificationError('Could not save this credential. Check the required fields and try again.')
  })
  const deleteCertification=useMutation({
    mutationFn:(id:number)=>api.delete(`/admin/certifications/${id}`),
    onSuccess:()=>{setCertificationError('');qc.invalidateQueries({queryKey:['admin-certifications']});qc.invalidateQueries({queryKey:['certifications']})},
    onError:()=>setCertificationError('Could not delete this credential. Please try again.')
  })
  const removeProject=useMutation({mutationFn:(id:number)=>api.delete(`/projects/${id}`),onSuccess:()=>qc.invalidateQueries({queryKey:['admin-projects']})})
  function logout(){localStorage.removeItem('oziel-token');location.reload()}
  return <div className="page"><header className="page-header"><div><p className="eyebrow">CONTROL ROOM</p><h1>Admin Dashboard</h1></div><button className="secondary" onClick={logout}>Lock system</button></header>
    <div className="admin-grid"><section className="panel admin-table"><div className="panel-head"><h2>Projects</h2><button className="icon-btn"><Plus size={16}/></button></div><table><thead><tr><th>Title</th><th>Status</th><th/></tr></thead><tbody>{(projects.data||[]).map(p=><tr key={p.id}><td>{p.title}</td><td><span className="category">{p.status}</span></td><td><button className="icon-btn danger" onClick={()=>removeProject.mutate(p.id)}><Trash2 size={15}/></button></td></tr>)}</tbody></table></section>
    <section className="panel admin-table"><div className="panel-head"><h2>Build Logs</h2><button className="icon-btn"><Plus size={16}/></button></div><table><thead><tr><th>Title</th><th>Category</th><th/></tr></thead><tbody>{(logs.data||[]).map(l=><tr key={l.id}><td>{l.title}</td><td><span className="category">{l.category}</span></td><td>—</td></tr>)}</tbody></table></section></div>
    <section className="panel admin-table admin-opportunities">
      <div className="panel-head"><div><p className="eyebrow">ALERTS ARE CREATED WHEN A NEW OPPORTUNITY IS ADDED</p><h2>Opportunity inbox</h2></div><button className="primary" onClick={()=>setShowOpportunityForm(true)}><Plus size={15}/> Add opportunity</button></div>
      <table><thead><tr><th>Opportunity</th><th>Company</th><th>Type</th><th>Email alert</th><th>Created</th></tr></thead><tbody>
        {(alerts.data||[]).map(a=><tr key={a.id}><td><b>{a.opportunity}</b></td><td>{a.company}</td><td>{a.channel}</td><td><span className={`category delivery-${a.status.toLowerCase()}`}>{a.status}</span>{a.lastError&&<small className="alert-error">{a.lastError}</small>}</td><td>{new Date(a.createdAt).toLocaleDateString('en-ZA')}</td></tr>)}
      </tbody></table>
      {!alerts.data?.length&&<p className="empty-alerts">No new-opportunity alerts yet. Add a listing to create the first alert.</p>}
    </section>
    <section className="panel admin-table admin-opportunities">
      <div className="panel-head"><div><p className="eyebrow">PRIVATE BY DEFAULT · PUBLISH ONLY WHAT YOU CHOOSE</p><h2>Credential vault</h2></div><button className="primary" onClick={()=>{setEditingCertification(null);setCertificationError('');setShowCertificationForm(true)}}><Plus size={15}/> Add credential</button></div>
      {certifications.isError&&<p className="error credential-admin-error">Could not load credentials. Please refresh and try again.</p>}
      <table><thead><tr><th>Credential</th><th>Issuer</th><th>Category</th><th>Earned</th><th>Visibility</th><th/></tr></thead><tbody>
        {(certifications.data||[]).map(c=><tr key={c.id}><td><b>{c.name}</b></td><td>{c.issuer}</td><td>{c.category}</td><td>{new Date(`${c.awardedOn}T00:00:00`).toLocaleDateString('en-ZA')}</td><td><span className={`category ${c.publiclyVisible?'credential-public':'credential-private'}`}>{c.publiclyVisible?'PUBLIC':'PRIVATE'}</span></td><td><button className="icon-btn" aria-label={`Edit ${c.name}`} onClick={()=>{setEditingCertification(c);setCertificationError('');setShowCertificationForm(true)}}><Pencil size={14}/></button><button className="icon-btn danger" aria-label={`Delete ${c.name}`} disabled={deleteCertification.isPending} onClick={()=>deleteCertification.mutate(c.id)}><Trash2 size={14}/></button></td></tr>)}
      </tbody></table>
      {!certifications.isPending&&!certifications.isError&&!certifications.data?.length&&<p className="empty-alerts">No credentials recorded. Add your actual certifications; nothing is invented or published automatically.</p>}
      {certificationError&&!showCertificationForm&&<p className="error credential-admin-error">{certificationError}</p>}
    </section>
    {showOpportunityForm&&<div className="modal-backdrop"><form className="modal" onSubmit={e=>{e.preventDefault();const f=new FormData(e.currentTarget);addOpportunity.mutate({name:String(f.get('name')),company:String(f.get('company')),type:String(f.get('type')) as Opportunity['type'],closingDate:String(f.get('closingDate'))||undefined,status:'WISHLIST',link:String(f.get('link'))})}}>
      <button type="button" className="icon-btn" onClick={()=>setShowOpportunityForm(false)}><X size={18}/></button><p className="eyebrow">ADD TO THE OPPORTUNITY TRACKER</p><h2>New opportunity</h2>
      <label>Role or programme<input name="name" required/></label><label>Company / provider<input name="company" required/></label>
      <label>Opportunity type<select name="type"><option>JOB</option><option>INTERNSHIP</option><option>LEARNERSHIP</option><option>BURSARY</option><option>SCHOLARSHIP</option><option>HACKATHON</option></select></label>
      <label>Closing date<input name="closingDate" type="date"/></label><label>Listing link from LinkedIn, Indeed, or the official company site<input name="link" type="url" required/></label>
      <p className="opportunity-source-help">Add the listing manually with its original link. The tracker labels LinkedIn and Indeed links automatically, and adding an opportunity queues your email alert.</p>
      {opportunityError&&<p className="error">{opportunityError}</p>}
      <button className="primary" disabled={addOpportunity.isPending}>{addOpportunity.isPending?'Adding and queuing alert...':'Add and notify me'}</button>
    </form></div>}
    {showCertificationForm&&<div className="modal-backdrop"><form key={editingCertification?.id??'new-certification'} className="modal" onSubmit={e=>{e.preventDefault();const f=new FormData(e.currentTarget);saveCertification.mutate({id:editingCertification?.id??null,body:{name:String(f.get('name')).trim(),issuer:String(f.get('issuer')).trim(),category:String(f.get('category')) as Certification['category'],awardedOn:String(f.get('awardedOn')),expiresOn:String(f.get('expiresOn'))||null,credentialUrl:String(f.get('credentialUrl')).trim()||null,credentialId:String(f.get('credentialId')).trim()||null,publiclyVisible:f.get('publiclyVisible')==='on'}})}}>
      <button type="button" className="icon-btn" aria-label="Close credential form" onClick={()=>{setShowCertificationForm(false);setEditingCertification(null);setCertificationError('')}}><X size={18}/></button>
      <p className="eyebrow">REAL CREDENTIALS ONLY</p><h2>{editingCertification?'Edit credential':'Add credential'}</h2>
      <label>Certificate name<input name="name" required maxLength={200} defaultValue={editingCertification?.name||''}/></label>
      <label>Issuer<input name="issuer" required maxLength={200} defaultValue={editingCertification?.issuer||''}/></label>
      <label>Category<select name="category" defaultValue={editingCertification?.category||'JAVA'}><option>JAVA</option><option>CLOUD</option><option>DATA</option><option>SECURITY</option><option>PROFESSIONAL</option><option>OTHER</option></select></label>
      <label>Date earned<input name="awardedOn" type="date" required defaultValue={editingCertification?.awardedOn||''}/></label>
      <label>Expiry date (if applicable)<input name="expiresOn" type="date" defaultValue={editingCertification?.expiresOn||''}/></label>
      <label>Credential verification URL (optional)<input name="credentialUrl" type="url" maxLength={2048} defaultValue={editingCertification?.credentialUrl||''}/></label>
      <label>Credential ID (optional)<input name="credentialId" maxLength={255} defaultValue={editingCertification?.credentialId||''}/></label>
      <label className="credential-visibility"><input name="publiclyVisible" type="checkbox" defaultChecked={editingCertification?.publiclyVisible||false}/> Publish this credential on my public profile</label>
      <p className="opportunity-source-help">Private is the default. Only publish credentials you want recruiters to see; use the issuer’s verification link when available.</p>
      {certificationError&&<p className="error">{certificationError}</p>}
      <button className="primary" disabled={saveCertification.isPending}>{saveCertification.isPending?'Saving…':editingCertification?'Save changes':'Save credential'}</button>
    </form></div>}
  </div>
}
