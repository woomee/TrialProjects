import { createAction, props } from '@ngrx/store';
import { Update } from '@ngrx/entity';
import { Comment } from '../../class/chat';

// import { Chat } from './Chat.model';

export const loadChats = createAction(
  '[Chat/API] Load Chats',
  (payload: { chats: Comment[] }) => ({ payload }),
);

export const loadChatsSuccess = createAction(
  '[Chat/API] Load Chats Success',
  (payload: { chats: Comment[] }) => ({ payload }),
);

export const loadChatsFail = createAction(
  '[Chat/API] Load Chats Fail',
  (payload?: { error: any }) => ({ payload }),
);

export const addChat = createAction(
  '[Chat/API] Add Chat',
  (payload: { chat: Comment }) => ({ payload }),
);

export const updateChat = createAction(
  '[Chat/API] Update Chat',
  (payload: { chat: Update<Comment> }) => ({ payload }),
);

export const deleteChat = createAction(
  '[Chat/API] Delete Chat',
  (payload: { id: string }) => ({ payload }),
);

export const writeChatSuccess = createAction(
  '[Chat/API] Write Chat Success',
  (payload?: { chats: Comment[] }) => ({ payload }),
);

export const writeChatFail = createAction(
  '[Chat/API] Write Chat Fail',
  (payload?: { error: any }) => ({ payload }),
);




// export const loadComments = createAction(
//   '[Comment/API] Load Comments', 
//   props<{ Comments: Comment[] }>()
// );

// export const addComment = createAction(
//   '[Comment/API] Add Comment',
//   props<{ Comment: Comment }>()
// );

// export const upsertComment = createAction(
//   '[Comment/API] Upsert Comment',
//   props<{ Comment: Comment }>()
// );

// export const addComments = createAction(
//   '[Comment/API] Add Comments',
//   props<{ Comments: Comment[] }>()
// );

// export const upsertComments = createAction(
//   '[Comment/API] Upsert Comments',
//   props<{ Comments: Comment[] }>()
// );

// export const updateComment = createAction(
//   '[Comment/API] Update Comment',
//   props<{ Comment: Update<Comment> }>()
// );

// export const updateComments = createAction(
//   '[Comment/API] Update Comments',
//   props<{ Comments: Update<Comment>[] }>()
// );

// export const deleteComment = createAction(
//   '[Comment/API] Delete Comment',
//   props<{ id: string }>()
// );

// export const deleteComments = createAction(
//   '[Comment/API] Delete Comments',
//   props<{ ids: string[] }>()
// );

// export const clearComments = createAction(
//   '[Comment/API] Clear Comments'
// );
